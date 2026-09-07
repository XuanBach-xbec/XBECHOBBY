import axiosClient from './axiosClient';

const gundamApi = {
  getAll() {
    return axiosClient.get('/gundams');
  },
  getById(id) {
    return axiosClient.get(`/gundams/${id}`);
  }
};

export default gundamApi;