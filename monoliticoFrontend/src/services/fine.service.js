import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/fine/');
}

const create = data => {
    return httpClient.post('/api/fine/', data);
}

const get = id => {
    return httpClient.get(`/api/fine/${id}`);
}

const getAllByClientId = id => {
    return httpClient.get(`/api/fine/get-all-clients/${id}`);
}

const update = data => {
    return httpClient.put('/api/fine/', data);
}

const remove = id => {
    return httpClient.delete(`/api/fine/${id}`
    );
}

export default { getAll, get, getAllByClientId, create, update, remove};