import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/fine/');
}

const create = data => {
    return httpClient.post('/fine/', data);
}

const get = id => {
    return httpClient.get(`/fine/${id}`);
}

const pay = (client_id,fine_id) => {
    return httpClient.post(`/fine/pay/${client_id}/${fine_id}`);
}

const getAllByClientId = id => {
    return httpClient.get(`/fine/get-all-clients/${id}`);
}

const update = data => {
    return httpClient.put('/fine/', data);
}

const remove = id => {
    return httpClient.delete(`/fine/${id}`
    );
}

export default { getAll, get, getAllByClientId, pay, create, update, remove};