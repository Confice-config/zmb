<template>
  <div style="height: 100vh; overflow: hidden; display: flex; align-items: center; justify-content: center;background-color: cornflowerblue;">
   <div style="display: flex; background-color: #fff; border-radius: 5px; overflow: hidden;">
    <div style="flex: 1; ">
      <img src="@/assets/imgs/login.png" alt="" width="100%">
    </div>
    <div style="flex: 1;display: flex;align-items: center;justify-content: center;">
      <el-form  style="width: 60%;" size="large" ref="formRef" :model="data.form" :rules="data.rules">
        <div style="font-size: 24px; text-align: center; font-weight: bold; margin-bottom: 20px;">欢迎登录后台管理系统</div>
        <el-form-item  prop="username">
          <el-input  prefix-icon="User" v-model="data.form.username" autocomplete="off" placeholder="请输入账号" ></el-input>
        </el-form-item >
        <el-form-item prop="password" >
          <el-input  prefix-icon="Lock" v-model="data.form.password" autocomplete="off" placeholder="请输入密码"  show-password></el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" style="width: 100%;" @click="login">登录</el-button>
        </el-form-item>
        <div style="display: flex;">
          <div style="flex: 1;"></div>
          <div style="flex: 1;text-align: right;">
            <el-link type="primary"><span style="font-size: 16px;">忘记密码</span></el-link>
          </div>
        </div>
      </el-form>
    </div>
   </div>
  </div>
</template>

<script setup>
import { reactive ,ref} from 'vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const  formRef=ref()
const data=reactive({
  form: {},
  rules: {
    username: [
      { required:true,message: '请输入账号',trigger: 'blur'},
      { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' },
    ],
    password: [
      { required:true,message: '请输入密码',trigger: 'blur'}
    ],
  }
})

const login = async () => {
  try {
    // 执行登录请求
    const loginRes = await request.post('/login', data.form);
    if (loginRes.code !== '200') {
      ElMessage.error(loginRes.msg);
      return;
    }

    // 存储 code_user（登录用户信息）
    const codeUser = loginRes.data || {};
    localStorage.setItem("code_user", JSON.stringify(codeUser));

    const userId = codeUser.id;
    const profileRes = await request.get('/profile-with-dept', { params: { userId } });
    localStorage.setItem("login_user", JSON.stringify(profileRes.data || {}));
    ElMessage.success("登录成功");
    router.push('/manager/home'); // 直接跳转（无需延迟）
  } catch (error) {
    const errorMsg = error.response?.data?.msg || "登录失败，请稍后再试";
    ElMessage.error(errorMsg);
  }
};
</script>

<style scoped>
/* 添加样式 */

</style>