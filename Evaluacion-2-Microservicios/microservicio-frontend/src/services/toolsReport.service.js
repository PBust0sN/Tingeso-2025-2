import httpClient from "../http-common";

const create = data => {
    return httpClient.post('/toolsReport/', data);
}

const get = id => {
    return httpClient.get(`/api/toolsReport/${id}`);
}

export default {get, create};