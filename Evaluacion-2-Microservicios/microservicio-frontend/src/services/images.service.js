import api from "../http-common";

const uploadImage = (file, filename) => {
    const formData = new FormData();
    formData.append('file', file);
    
    return api.post(`/images/upload${filename ? '?filename=' + filename : ''}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

const getImage = imageName => {
    return api.get(`/images/${imageName}`, { responseType: 'blob' });
}

export default { uploadImage, getImage };