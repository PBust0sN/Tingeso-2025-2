import httpClient from "../http-common";

const create = data => {
    return httpClient.post('/toolsloansreports/', data);
}

const getToolsIdByLoanId = id => {
    return httpClient.get(`/api/toolsloansreports/getools/${id}`);
}


export default {create, getToolsIdByLoanId};