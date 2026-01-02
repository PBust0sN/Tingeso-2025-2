import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/clients/loans/');
}

const create = data => {
    return httpClient.post('/clients/loansts/', data);
}

const get = id => {
    return httpClient.get(`/api/clients/loans/${id}`);
}

const update = data => {
    return httpClient.put('/clients/loans/', data);
}

const remove = id => {
    return httpClient.delete(`/api/clients/loans/${id}`
    );
}

export default { getAll, get, create, update, remove};