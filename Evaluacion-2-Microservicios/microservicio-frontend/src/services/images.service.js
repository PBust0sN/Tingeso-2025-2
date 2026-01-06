import api from "../http-common";

const uploadImage = (data, filename) => {
    const params = new URLSearchParams();
    if (filename) {
        params.append('filename', filename);
    }
    
    return api.post(`/images/upload${filename ? '?filename=' + filename : ''}`, data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

const getImage = imageName => {
    return api.get(`/images/${imageName}`, { responseType: 'blob' });
}

export default { uploadImage, getImage };