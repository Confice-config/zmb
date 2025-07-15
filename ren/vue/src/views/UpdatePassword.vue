<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 100vh;">
<div class="card" style="width: 50%">
  <div style="font-size: 16px;">修改密码</div>
  <el-form ref="fromRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 20px 30px 20px 0">
    <el-form-item  prop="password"  label="原密码">
      <el-input v-model="data.user.password"  show-password/>
    </el-form-item>
    <el-form-item  prop="newpassword"  label="新密码">
      <el-input v-model="data.user.newpassword"  />
    </el-form-item>
    <el-form-item  prop="new2password"  label="确认密码">
      <el-input v-model="data.user.new2password"  />
    </el-form-item>
  </el-form>
  <div style="text-align: center">
    <el-button type="primary" style="padding: 20px 40px" @click="uodatepassword">保存</el-button>
  </div>
</div>
  </div>
</template>
<script setup>
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user')||'{}'),
  login:JSON.parse(localStorage.getItem('login_user')||'{}'),
  rules:{
    password: [
      { required:true,message: '请输入原密码',trigger: 'blur'}
    ],
    newpassword: [
      { required:true,message: '请输入新密码',trigger: 'blur'}
    ],
    new2password: [
      { required:true,message: '请确认新密码',trigger: 'blur'}
    ],
  }
})

const fromRef = ref()

const uodatepassword = () =>{
  fromRef.value.validate(valid  => {
    if (valid){
      request.post('/updatepassword',data.user).then((res)=> {
        if(res.code==200){
          ElMessage.success("修改成功")
          setInterval(()=>{
            localStorage.removeItem('code_user')
            location.href = "/login";
          },500)
        }else {
          ElMessage.error(res.msg)
        }
      })
    }
  })

}
</script>

