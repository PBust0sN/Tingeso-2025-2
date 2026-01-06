import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/records/');
}

const create = data => {
    return httpClient.post('/records/', data);
}

const get = id => {
    return httpClient.get(`/records/${id}`);
}

const update = data => {
    return httpClient.put('/records/', data);
}

const remove = id => {
    return httpClient.delete(`/records/${id}`
    );
}

export default { getAll, get, create, update, remove};