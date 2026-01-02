import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/loansReport/');
}

const create = data => {
    return httpClient.post('/loansReport/', data);
}

const remove = id => {
    return httpClient.delete(`/loansReport/${id}`);
}

const getAllByReportId = id => {
    return httpClient.get(`/loansReport/${id}`);
}

const getById = id => {
    return httpClient.get(`/loansReport/getbyid/${id}`);   
}
export default { getAll, create, remove, getAllByReportId, getById};