import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/tools/');
}

const create = data => {
    return httpClient.post('/tools/', data);
}

const get = id => {
    return httpClient.get(`/api/tools/${id}`);
}

const update = data => {
    return httpClient.put('/tools/', data);
}

const remove = id => {
    return httpClient.delete(`/api/tools/${id}`
    );
}

const updateState = (id, state) => {
    return httpClient.put(`/api/tools/update/state/${id}?state=${state}`);
}

const updateStock = id => {
    return httpClient.put(`/api/tools/update/stock/${id}`);
}

const getTenTools = () => {
    return httpClient.get('/tools/topTen');
}

export default { getAll, get, create, update, remove, updateState, updateStock, getTenTools };