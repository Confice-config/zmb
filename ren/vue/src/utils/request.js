import axios  from "axios";
import {ElMessage} from "element-plus";

const request =axios.create({
    baseURL: "http://localhost:9999",
    timeout: 3000
})

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    let user = JSON.parse(localStorage.getItem('code_user')||'{}');
    config.headers['token'] =  user.token
    return config;
},error =>{
    return Promise.reject(error);
    });

request.interceptors.response.use(
    response => {
        let res = response.data;
        if (res.code === 'string') {
            res = res?JSON.parse(res) : res
        }
        if (res.code === '401') {
            ElMessage.error(res.msg)
        }else
        return res;
    },
    error =>{
        if (error.response.status === 404) {
            ElMessage.error('未找到对应接口')
        }else if (error.response.status === 500) {
            ElMessage.error('系统异常，请查看后台报错')
        }else {
            console.error(error.message)
        }
        return Promise.reject(error)
    }
)

export default request;