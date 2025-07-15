<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 100vh;">
  <div  class="card" style="width: 50%">
    <div style="font-size: 16px;">个人信息</div>
    <el-form  ref="fromRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
      <el-form-item  prop="employeeName"  label="姓名">
        <el-input v-model="data.login.employeeName" />
      </el-form-item>
      <el-form-item  prop="employeeGender"  label="性别" >
        <el-input v-model="data.login.employeeGender" />
      </el-form-item>
      <el-form-item  prop="employeeAge"  label="年龄" >
        <el-input v-model="data.login.employeeAge" />
      </el-form-item>
      <el-form-item  prop="employeePhone"  label="手机号" >
        <el-input v-model="data.login.employeePhone" />
      </el-form-item>
      <el-form-item  prop="employeeEmail"  label="邮箱" >
        <el-input v-model="data.login.employeeEmail" />
      </el-form-item>
      <el-form-item  prop="employeeDepartmentName"  label="部门" >
        <el-input v-model="data.login.employeeDepartmentName" :disabled="true"/>
      </el-form-item>
      <el-form-item  prop="employeePosition"  label="职位" >
        <el-input v-model="data.login.employeePosition" :disabled="true" />
      </el-form-item>
      <el-form-item  prop="hireDate"  label="入职日期"  >
        <el-input v-model="data.login.hireDate" :disabled="true"/>
      </el-form-item>
      <el-form-item  prop="employeeStatus"  label="状态" >
        <el-input v-model="data.login.employeeStatus" :disabled="true"/>
      </el-form-item>
      <el-form-item  prop="avatar"  label="头像" >
        <el-upload
          action="http://localhost:9999/files/upload"
          :headers="{token : data.user.token}"
          :on-success="handleFileSuccess"
          list-type="picture"
          >
          <el-button type="primary">上传头像</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" style="padding: 20px 40px" @click="update">保存</el-button>
    </div>
  </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user')||'{}'),
  login:JSON.parse(localStorage.getItem('login_user')||'{}'),
})

const handleFileSuccess = (res) =>{
  data.login.employeeAvatar=res.data;
}
const update = () =>{
let url='/profile-with-update'
  request.put(url,data.login).then(res=>{
    if (res.code === '200') {
      ElMessage.success("保存成功")
      localStorage.setItem("login_user",JSON.stringify(data.login))
      emit('updateUser')
    }
  })
}
const emit = defineEmits(['updateUser'])
</script>