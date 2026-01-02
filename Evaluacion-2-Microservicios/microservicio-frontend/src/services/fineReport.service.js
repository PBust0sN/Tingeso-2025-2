import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/fineReport/');
}

const create = data => {
    return httpClient.post('/fineReport/', data);
}

const remove = id => {
    return httpClient.delete(`/fineReport/${id}`);
}

const getAllByReportId = id => {
    return httpClient.get(`/fineReport/${id}`);
}

export default { getAll, create, remove, getAllByReportId};