<template>
  <div>
<!--    头部-->
    <div style="height: 60px;display: flex;">
      <div style="display: flex; align-items: center; padding-left: 20px;background-color: #3a456b;width: 250px">
        <img src="../assets/imgs/智能化.png" alt="" style="width: 50px; height: 50px;border-radius: 50%;" />
        <span style="font-size: 20px;font-weight: bold;margin: 5px;color: #f1f1f1">人事管理系统</span>
      </div>
      <div style="flex: 1;border-bottom: 1px solid #ddd;display: flex;align-items: center;padding-left: 20px">
        <span style="font-size: 20px;margin-right: 5px" @click="router.push('/manager/home')">人事管理系统 /</span><span style="font-size: 20px">{{ router.currentRoute.value.name }}</span>
      </div>
      <div style="width: fit-content;display: flex;align-items: center;padding-right: 20px;border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex; align-items: center;">
              <img v-if="data.user?.avatar" style="width: 40px;height: 40px;border-radius: 50%" :src="data.user?.avatar" />
              <img v-else style="width: 40px;height: 40px;border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
              <span style="margin-left: 5px">{{data.login.employeeName || "未知用户" }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="personme">个人信息</el-dropdown-item>
              <el-dropdown-item @click="uppass">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
<!--    头部结束-->
    <!-- 下方区域开始 -->
    <div style="flex: 1; display: flex; flex-direction: row; background-color: #f2f4ff; min-height: calc(100vh - 60px);">
      <!-- 菜单区域开始 -->
      <div style="width: 250px;">
        <el-menu router :default-openeds="['1']" :default-active="router.currentRoute.value.path" style="height: 100%; background-color: #3a456b; border-color: #3a456b;">
          <el-menu-item index="/manager/home"><el-icon><House /></el-icon>首页</el-menu-item>
          <el-menu-item index="/manager/employee" v-if="data.user.roleId !=3"><el-icon><UserFilled /></el-icon>员工档案</el-menu-item>
          <el-menu-item index="/manager/attendance"><el-icon><Bell /></el-icon>考勤打卡</el-menu-item>
          <el-menu-item index="/manager/salary"><el-icon><StarFilled /></el-icon>薪资发放</el-menu-item>
          <el-menu-item index="/manager/department" v-if="data.user.roleId !=3"><el-icon><Crop /></el-icon>部门管理</el-menu-item>
          <el-menu-item index="/manager/contract"><el-icon><Edit /></el-icon>合同管理</el-menu-item>
          <el-menu-item index="/manager/training"><el-icon><PieChart /></el-icon>培训管理</el-menu-item>
          <el-sub-menu index="4">
            <template #title>
              <el-icon><location /></el-icon>
              <span>审批中心</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/manager/leave">请假管理</el-menu-item>
              <el-menu-item index="/manager/Resignation">离职管理</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </div>
      <!-- 菜单区域结束 -->

      <!-- 数据区开始 -->
      <div style="flex: 1; padding: 10px;">
        <RouterView @updateUser="updateUser" />
      </div>
      <!-- 数据区结束 -->
    </div>
    <!-- 下方区域结束 -->



  </div>
</template>

<script setup>

import router from "@/router/index.js";
import {reactive,onMounted} from "vue";
import axios from "axios";
import request from "@/utils/request.js";

const data=reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  login: JSON.parse(localStorage.getItem('login_user') || "{}"),
  employeeName: ""
})



const logout = () => {
  // 清除所有相关存储
  localStorage.removeItem('code_user')
  localStorage.removeItem('login_user')
  sessionStorage.clear()

  // 强制刷新清除内存数据
  window.location.href = "/login?redirect=" + encodeURIComponent(router.currentRoute.value.fullPath)
}



onMounted(() => {
  const employeeId = data.user?.employeeId;
  // 同时校验null和空字符串（避免空字符串情况）
  if (employeeId === null || employeeId === "") {
    data.employeeName = "未知用户";
    return;
  }
  // 确保employeeId是数字字符串或数字（如"123"或123）
  if (typeof employeeId !== "number" && isNaN(Number(employeeId))) {
    data.employeeName = "未知用户";
    return;
  }
  fetchEmployeeName(String(employeeId)); // 转为字符串传递（避免null）
});



const fetchEmployeeName = async () => {
  try {
    const res = await axios.get(`http://localhost:9999/employee/select/${data.user.employeeId}`);
    data.employeeName = res.data.data;
  } catch (err) {
    console.error("获取姓名失败:", err);
    data.employeeName = "未知用户";
  }
  return data.employeeName;
};

const personme =() =>{

  router.push({ name: '个人信息' })

}

const updateUser = () =>{
  data.login = JSON.parse(localStorage.getItem('login_user') || '{}')
}

const uppass = () =>{
  router.push('/manager/updatePassword')
}
</script>

<style>
.el-menu{
  background-color: #3a456b;
  border-color: #3a456b;
}
.el-sub-menu__title{
  color: white;
}
.el-menu-item{
  height: 50px;
  color:white;
}
.el-menu-item-group{
  height: 50px;
  color:white;
}
.el-menu .is-active{
  background-color: #537bee;
  color: white;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}
.el-menu-item:hover{
  background-color: #7a9fff;
  color: white;
}
.el-tooltip__trigger{
  outline: none;
}
.el-dropdown{
  cursor: pointer;
}
.el-menu--inline .el-menu-item{
  padding-left: 50px !important;
}
</style>