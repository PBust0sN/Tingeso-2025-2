import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/toolsRanking/');
}

const create = data => {
    return httpClient.post('/toolsRanking/', data);
}

const remove = id => {
    return httpClient.delete(`/api/toolsRanking/${id}`
    );
}

const getAllByReportId = id => {
    return httpClient.get(`/api/toolsRanking/${id}`);
}

export default { getAll, create, remove, getAllByReportId};