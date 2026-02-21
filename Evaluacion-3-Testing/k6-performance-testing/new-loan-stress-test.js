import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 100 },
    { duration: '30s', target: 200 },
    { duration: '30s', target: 400 },
    { duration: '30s', target: 800 },    
    { duration: '30s', target: 1000 },    
    { duration: '30s', target: 1500 },
    { duration: '30s', target: 2000 },
    { duration: '30s', target: 2500 },       
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ["p(95)<30000"],
  },
};

const API_URL = "http://localhost:8090";

// Credenciales
const USERNAME = "gonzalo";
const PASSWORD = "admin";

function getToken() {
  const loginUrl = `${API_URL}/api/clients/login?username=${encodeURIComponent(USERNAME)}&password=${encodeURIComponent(PASSWORD)}`;

  const response = http.post(loginUrl);

  check(response, {
    "login success": (r) => r.status === 200 || r.status === 201,
  });

  if (response.status !== 200 && response.status !== 201) {
    throw new Error(`Login failed: ${response.status} - ${response.body}`);
  }

  const data = response.json();
  const token = data.access_token || data.token || data.jwtToken;

  if (!token) {
    throw new Error("No token in login response");
  }

  return token;
}

function getLoansCount(token) {
  const headers = {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  const res = http.get(`${API_URL}/api/loans/`, { headers });

  if (res.status === 200) {
    const loans = res.json();
    return Array.isArray(loans) ? loans.length : 0;
  }

  return 0;
}

export function setup() {
  const token = getToken();
  const initialCount = getLoansCount(token);
  
  console.log(`✅ Setup completado`);
  console.log(`   Token obtenido`);
  console.log(`   Préstamos iniciales en el sistema: ${initialCount}`);
  
  return { 
    token,
    initialLoanCount: initialCount
  };
}

export default function (data) {
  const headers = {
    Authorization: `Bearer ${data.token}`,
    "Content-Type": "application/json",
  };

  // Datos aleatorios
  const testData = {
    staff_id: 1,
    client_id: Math.floor(Math.random() * 4) + 1,
    tools_id: [
      Math.floor(Math.random() * 30) + 1,
      Math.floor(Math.random() * 30) + 1,
      Math.floor(Math.random() * 30) + 1,
    ],
    days: Math.floor(Math.random() * 30) + 1,
  };

  const loanUrl = `${API_URL}/api/loans/new`;

  // ====== PASO 1: CREAR PRÉSTAMO ======
  const createRes = http.post(loanUrl, JSON.stringify(testData), { headers });

  check(createRes, {
    "loan created": (r) => r.status === 201 || r.status === 200,
  });

  if (createRes.status !== 200 && createRes.status !== 201) {
    return;
  }

}

export function teardown(data) {
  const headers = {
    Authorization: `Bearer ${data.token}`,
    "Content-Type": "application/json",
  };

  // Contar préstamos finales
  const finalCount = getLoansCount(data.token);
  const initialCount = data.initialLoanCount;
  const newLoansCount = finalCount - initialCount;

  console.log(`\n🧹 TEARDOWN:`);
  console.log(`   Préstamos iniciales: ${initialCount}`);
  console.log(`   Préstamos finales: ${finalCount}`);
  console.log(`   Nuevos préstamos creados: ${newLoansCount}`);

  if (newLoansCount <= 0) {
    console.log(`⚠️  No hay nuevos préstamos para eliminar.`);
    return;
  }

  // Obtener lista de préstamos y eliminar los últimos (los nuevos)
  const res = http.get(`${API_URL}/api/loans/`, { headers });

  if (res.status !== 200) {
    console.log(`❌ No se pudo obtener lista de préstamos: ${res.status}`);
    return;
  }

  const loans = res.json();

  if (!Array.isArray(loans) || loans.length === 0) {
    console.log(`⚠️  No hay préstamos en la lista.`);
    return;
  }

  // Devolver y luego eliminar los últimos N préstamos (donde N = newLoansCount)
  const loansToProcess = loans.slice(-newLoansCount);

  console.log(`   Devolviendo y eliminando ${loansToProcess.length} préstamos...`);

  let returnedCount = 0;
  let deletedCount = 0;
  let failedCount = 0;

  loansToProcess.forEach((loan, index) => {
    const loanId = loan.id || loan.loanId || loan.loan_id;

    if (!loanId) {
      console.log(`   ⚠️  [${index + 1}/${loansToProcess.length}] No se pudo extraer ID del préstamo`);
      failedCount++;
      return;
    }

    // PASO 1: DEVOLVER PRÉSTAMO
    const returnRes = http.post(`${API_URL}/api/loans/return`, JSON.stringify(loan), { headers });

    if (returnRes.status === 200 || returnRes.status === 201) {
      returnedCount++;
      console.log(`   ✅ [${index + 1}/${loansToProcess.length}] Préstamo ${loanId} devuelto`);
    } else {
      console.log(`   ⚠️  [${index + 1}/${loansToProcess.length}] Préstamo ${loanId} no se devolvió: ${returnRes.status}`);
    }

    // PASO 2: ELIMINAR PRÉSTAMO
    const deleteRes = http.del(`${API_URL}/api/loans/${loanId}`, null, { headers });

    if (deleteRes.status === 204 || deleteRes.status === 200) {
      deletedCount++;
      console.log(`   ✅ [${index + 1}/${loansToProcess.length}] Préstamo ${loanId} eliminado`);
    } else {
      failedCount++;
      console.log(`   ❌ [${index + 1}/${loansToProcess.length}] Fallo al eliminar ${loanId}: ${deleteRes.status}`);
    }
  });

  console.log(`\n📊 RESUMEN: ${returnedCount} devueltos, ${deletedCount} eliminados, ${failedCount} fallidos`);
}

