import httpClient from "../http-common";

const getAll = () => {
    return httpClient.get('/api/toolsRanking/');
}

const create = data => {
    return httpClient.post('/api/toolsRanking/', data);
}

const remove = id => {
    return httpClient.delete(`/api/toolsRanking/${id}`
    );
}

export default { getAll, get, create, update, remove, getToolsIdByLoanId};