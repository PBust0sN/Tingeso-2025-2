import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/fees/');
}

const create = data => {
    return httpClient.post('/fees/', data);
}

const get = id => {
    return httpClient.get(`/fees/${id}`);
}

const update = data => {
    return httpClient.put('/fees/', data);
}

const remove = id => {
    return httpClient.delete(`/fees/${id}`
    );
}

export default { getAll, get, create, update, remove};