import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/staff/');
}

const create = data => {
    return httpClient.post('/staff/', data);
}

const get = id => {
    return httpClient.get(`/staff/${id}`);
}

const update = data => {
    return httpClient.put('/staff/', data);
}

const remove = id => {
    return httpClient.delete(`/staff/${id}`
    );
}

export default { getAll, get, create, update, remove};