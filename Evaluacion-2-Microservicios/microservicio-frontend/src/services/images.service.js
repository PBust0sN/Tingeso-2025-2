import axios from "axios";

// Cliente HTTP sin Content-Type forzado para permitir multipart/form-data
const uploadClient = axios.create({
    baseURL: '/'
});

// Copiar los interceptores del httpClient para autenticación
import api from "../http-common";

uploadClient.interceptors.request.use(
    api.interceptors.request.handlers[0].fulfilled,
    api.interceptors.request.handlers[0].rejected
);

uploadClient.interceptors.response.use(
    api.interceptors.response.handlers[0].fulfilled,
    api.interceptors.response.handlers[0].rejected
);

const uploadImage = (file, filename) => {
    const formData = new FormData();
    formData.append('file', file);
    
    return uploadClient.post(`/images/upload?filename=${filename}`, formData);
}

const getImage = imageName => {
    return api.get(`/images/${imageName}`, { responseType: 'blob' });
}

export default { uploadImage, getImage };