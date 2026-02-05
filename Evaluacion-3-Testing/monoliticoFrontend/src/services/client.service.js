import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/clients/');
}

const create = data => {
    return httpClient.post('/api/clients/', data);
}

const get = id => {
    return httpClient.get(`/api/clients/${id}`);
}

const update = data => {
    return httpClient.put('/api/clients/', data);
}

const remove = id => {
    return httpClient.delete(`/api/clients/${id}`
    );
}

const getByRut = rut => {
    return httpClient.get(`/api/clients/getbyrut/${rut}`);
};

const login = (username, password) => {
    return httpClient.post('/api/clients/login', null, {
        params: { username, password }
    }).then(response => {
        // Manejar el cuerpo completo de la respuesta
        return response.data; // Retornar los datos completos
    }).catch(error => {
        console.error("Error en el login:", error);
        throw error;
    });
};

const refreshToken = (refreshToken) => {
    return httpClient.post('/realms/toolRent/protocol/openid-connect/token', null, {
        params: {
            client_id: 'toolrent-Frontend', // Replace with your client ID
            grant_type: 'refresh_token',
            refresh_token: refreshToken
        }
    });
};


export default { getAll, get, create, update, remove, getByRut, login, refreshToken };