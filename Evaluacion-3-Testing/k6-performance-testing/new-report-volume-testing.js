import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  stages: [
    { duration: "1m", target: 20 },   // Rampa a 20 VUs
    { duration: "1m", target: 50 },   // Sube a 50 VUs
    { duration: "1m", target: 75 },  // Sube a 100 VUs
    { duration: "1m", target: 100 },  // Mantén 100 VUs
    { duration: "40s", target: 100 },
    { duration: "1m", target: 0 },    // Baja a 0
  ],
  thresholds: {
    http_req_failed: ["rate<0.1"],    // Permitir 10% de fallos
    http_req_duration: ["p(95)<2000"], // 95% de requests < 2s
  },
};

const API_URL = "http://localhost:8090";

// Credenciales
const USERNAME = "gonzalo";
const PASSWORD = "admin";

// 5 clientes con 2 préstamos cada uno = 6 préstamos totales
const CLIENT_IDS = [1, 2, 3];

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

export function setup() {
  const token = getToken();

  const headers = {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  // CONTAR DATOS INICIALES
  const initialReportsRes = http.get(`${API_URL}/api/reports/`, { headers });
  const initialReportsCount = initialReportsRes.status === 200 ? 
    (Array.isArray(initialReportsRes.json()) ? initialReportsRes.json().length : 0) : 0;

  const initialLoanReportsRes = http.get(`${API_URL}/api/loansReport/`, { headers });
  const initialLoanReportsCount = initialLoanReportsRes.status === 200 ? 
    (Array.isArray(initialLoanReportsRes.json()) ? initialLoanReportsRes.json().length : 0) : 0;

  const initialToolsReportsRes = http.get(`${API_URL}/api/toolsReport/`, { headers });
  const initialToolsReportsCount = initialToolsReportsRes.status === 200 ? 
    (Array.isArray(initialToolsReportsRes.json()) ? initialToolsReportsRes.json().length : 0) : 0;

  const initialToolsLoanReportsRes = http.get(`${API_URL}/api/toolsLoanReport/`, { headers });
  const initialToolsLoanReportsCount = initialToolsLoanReportsRes.status === 200 ? 
    (Array.isArray(initialToolsLoanReportsRes.json()) ? initialToolsLoanReportsRes.json().length : 0) : 0;

  const createdLoans = [];
  for (const clientId of CLIENT_IDS) {
    for (let i = 0; i < 2; i++) {
      const loanData = {
        staff_id: 1,
        client_id: clientId,
        tools_id: [
          Math.floor(Math.random() * 30) + 1,
          Math.floor(Math.random() * 30) + 1,
        ],
        days: Math.floor(Math.random() * 20) + 5,
      };

      const createRes = http.post(
        `${API_URL}/api/loans/new`,
        JSON.stringify(loanData),
        { headers }
      );

      if (createRes.status === 200 || createRes.status === 201) {
        createdLoans.push({ clientId });
      }
    }
  }

  return {
    token,
    loans: createdLoans,
    clientIds: CLIENT_IDS,
    initialLoansCount: 0,
    initialReportsCount,
    initialLoanReportsCount,
    initialToolsReportsCount,
    initialToolsLoanReportsCount,
  };
}

export default function (data) {
  const headers = {
    Authorization: `Bearer ${data.token}`,
    "Content-Type": "application/json",
  };

  // Seleccionar un cliente aleatorio de los 5 creados
  const clientId = data.clientIds[Math.floor(Math.random() * data.clientIds.length)];

  // PASO 1: OBTENER TODOS LOS PRÉSTAMOS
  const loansRes = http.get(`${API_URL}/api/loans/`, { headers });

  check(loansRes, {
    "get all loans": (r) => r.status === 200,
  });

  if (loansRes.status !== 200) {
    return;
  }

  const allLoans = loansRes.json();

  if (!Array.isArray(allLoans) || allLoans.length === 0) {
    return;
  }

  // PASO 2: FILTRAR PRÉSTAMOS DEL CLIENTE SELECCIONADO
  const clientLoans = allLoans.filter((l) => l.clientId === clientId);

  if (clientLoans.length === 0) {
    return;
  }

  // PASO 3: CREAR REPORTE
  const reportRes = http.post(
    `${API_URL}/api/reports/`,
    JSON.stringify({
      loanIdReport: true,
      clientIdReport: clientId,
    }),
    { headers }
  );

  check(reportRes, {
    "create report": (r) => r.status === 200 || r.status === 201,
  });

  if (reportRes.status !== 200 && reportRes.status !== 201) {
    return;
  }

  const report = reportRes.json();
  const reportId = report?.reportId;

  if (!reportId) {
    return;
  }

  // PASO 4: PROCESAR CADA PRÉSTAMO DEL CLIENTE
  for (const loan of clientLoans) {
    // PASO 4.1: CREAR LOAN REPORT
    const loanReportRes = http.post(
      `${API_URL}/api/loansReport/`,
      JSON.stringify({
        reportId: reportId,
        clientId: loan.clientId,
        loanType: loan.loanType,
        amount: loan.amount,
        deliveryDate: loan.deliveryDate,
        returnDate: loan.returnDate,
        date: loan.date,
        staffId: loan.staffId,
        extraCharges: loan.extraCharges || 0,
      }),
      { headers }
    );

    check(loanReportRes, {
      "create loan report": (r) => r.status === 200 || r.status === 201,
    });

    if (loanReportRes.status !== 200 && loanReportRes.status !== 201) {
      continue;
    }

    const loanReport = loanReportRes.json();
    const loanReportId = loanReport?.loanReportId;

    if (!loanReportId) {
      continue;
    }

    // PASO 4.2: OBTENER TOOLS ASOCIADOS AL PRÉSTAMO
    const toolsLoansRes = http.get(
      `${API_URL}/api/tools/loan/toolsIDs/${loan.loanId}`,
      { headers }
    );

    if (toolsLoansRes.status !== 200) {
      continue;
    }

    const toolIds = toolsLoansRes.json();

    if (!Array.isArray(toolIds) || toolIds.length === 0) {
      continue;
    }

    // PASO 4.3: PROCESAR CADA TOOL
    for (const toolId of toolIds) {
      // PASO 4.3.1: OBTENER DETALLES DEL TOOL
      const toolRes = http.get(`${API_URL}/api/tools/${toolId}`, { headers });

      if (toolRes.status !== 200) {
        continue;
      }

      const tool = toolRes.json();

      // PASO 4.3.2: CREAR TOOLS REPORT
      const toolReportRes = http.post(
        `${API_URL}/api/toolsReport/`,
        JSON.stringify({
          toolName: tool.tool_name || tool.toolName,
          category: tool.category,
          loanNount: tool.loan_count || tool.loanCount || 0,
        }),
        { headers }
      );

      if (toolReportRes.status !== 200 && toolReportRes.status !== 201) {
        continue;
      }

      const toolReport = toolReportRes.json();
      const toolIdReport = toolReport?.toolIdReport;

      if (!toolIdReport) {
        continue;
      }

      // PASO 4.3.3: CREAR TOOLS LOAN REPORT (RELACIÓN)
      http.post(
        `${API_URL}/api/toolsloansreports/`,
        JSON.stringify({
          loanId: loanReportId,
          toolId: toolIdReport,
        }),
        { headers }
      );
    }
  }

  sleep(1);
}