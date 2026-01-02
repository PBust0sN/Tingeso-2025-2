import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/reports/');
}

const create = data => {
    return httpClient.post('/reports/', data);
}

const get = id => {
    return httpClient.get(`/api/reports/${id}`);
}

const getAllByClientId = id => {
    return httpClient.get(`/api/reports/get-all-client/${id}`);
}

const update = data => {
    return httpClient.put('/reports/', data);
}

const remove = id => {
    return httpClient.delete(`/api/reports/${id}`
    );
}

export default { getAll, get, getAllByClientId, create, update, remove};