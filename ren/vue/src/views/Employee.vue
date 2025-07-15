<template>
  <div class="app-container">
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @click="load" style="width: 240px;margin-right: 10px" v-model="data.name" placeholder="请输入姓名进行查询" :prefix-icon="Search"></el-input>
      <el-input clearable @click="load" style="width: 240px;margin-right: 10px" v-model="data.departmentId" placeholder="请输入部门id进行查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
<div class="card" style="margin-bottom: 5px" >
      <el-button type="success" @click="handAdd" >新增</el-button>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
      <el-button type="info" @click="exportData">批量导出</el-button>
      <el-upload
      action="http://localhost:9999/employee/import"
      :show-file-list="false"
      :on-success="handleImportSuccess"
      style="display: inline-block;margin-left: 10px"
      >
        <el-button type="info" @click="">批量导入</el-button>
      </el-upload>
    </div>
    <div class="card" style="margin-bottom: 5px " >
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleCurrentChange" :header-cell-style="{color :'#333',backgroundColor:'#eaf4ff'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="id" label="员工id"  />
        <el-table-column prop="name" label="姓名"  />
        <el-table-column prop="gender" label="性别"  />
        <el-table-column prop="age" label="年龄"  />
        <el-table-column prop="phone" label="手机号"  />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="departmentName" label="部门名称"  />
        <el-table-column prop="hireDate" label="入职日期"  />
        <el-table-column prop="position" label="职位"  />
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作">
          <template #default="scope">
          <el-button type="primary" icon="Edit" circle  @click="handleEdit(scope.row)"></el-button>
          <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total,sizes, prev, pager, next,jumper"
          :total="data.total"
          :page-sizes="[5,10,15]"
          @current-change="load"
          @size-change="load"
      />
    </div>
    <el-dialog title="员工信息" v-model="data.formVisible"  width="35%" destroy-on-close>
      <el-form  ref="fromRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item  prop="name"  label="姓名">
          <el-input v-model="data.form.name" />
        </el-form-item>
        <el-form-item  prop="gender" label="性别">
          <el-radio-group v-model="data.form.gender">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item  prop="age"  label="年龄">
          <el-input v-model="data.form.age" />
        </el-form-item>
        <el-form-item  prop="phone"   label="手机号">
          <el-input v-model="data.form.phone" />
        </el-form-item>
        <el-form-item  prop="email" label="邮箱">
          <el-input v-model="data.form.email" />
        </el-form-item>
        <el-form-item  prop="departmentId"  label="所属部门">
          <el-select
              v-model="data.form.departmentId"
              placeholder="部门名称"
              style="width: 35%;"
          >
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>


        <el-form-item  prop="hireDate" label="入职时间">
          <el-col :span="11">
            <el-date-picker v-model="data.form.hireDate" type="date" placeholder="Pick a date"   value-format="YYYY-MM-DD" style="width: 100%" />
          </el-col>
        </el-form-item>
        <el-form-item  prop="position" label="职位">
          <el-input v-model="data.form.position" />
        </el-form-item>
        <el-form-item    label="状态">
          <el-radio-group v-model="data.form.status">
            <el-radio :label="'在职'">在职</el-radio>
            <el-radio :label="'离职'">离职</el-radio>
            <el-radio :label="'退休'">退休</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive,ref} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage,ElMessageBox} from "element-plus";


const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user')||'{}'),
  name:'',
  departmentId: '',
  pageNum: 1,
  pageSize: 15,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  categoryData:[],
  rules:{
    name:[
      {required:true , pattern: /^[a-zA-Z\u4e00-\u9fa5\s'-]{2,20}$/, message: '姓名仅允许中英文、空格、\'或-，长度2-20', trigger: 'blur'}
    ],
    gender:[
      {required:true ,message:'请选择性别',trigger:'change'}
    ],
    age:[
      {required:true ,message:'请输入年龄',trigger:'blur'}
    ],
    phone:[
      { required: true, message: '请输入手机号', trigger: ['blur', 'change'] },
      {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位中国大陆手机号', trigger: 'blur'}
    ],
    email:[
      {pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入正确的邮箱格式', trigger: 'blur'}
    ],
    departmentId:[
      {required:true ,message:'请选择所属部门',trigger:'change'}
    ],
    hireDate:[
      {required:true ,message:'请选择入职时间',trigger:'change',type:"date"}
    ],
    position:[
      {required:true ,message:'请输入职位',trigger:'blur'}
    ]
  },
  rows:[],
  ids:[]
})

const fromRef = ref()

const load = () => {
  request.get("/employee/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name:data.name,
      departmentId:data.departmentId
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list;
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }

  })
}

load()


const reset = () => {
  data.name = '',
  data.departmentId=''
  load()
}


const handAdd = () => {
  data.formVisible = true
  data.form={}
}

const add = () =>{
  fromRef.value.validate(valid => {
    if (valid) {
      request.post("/employee/add", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('新增成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row) =>{
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const update = () =>{
  fromRef.value.validate(valid => {
    if (valid) {
      request.put("/employee/update", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () =>{
  data.form.id ? update() : add()
}

const del = (id) =>{
  ElMessageBox.confirm('您确认删除该员工吗？删除后无法恢复！','删除确认',{type:"warning"}).then(res =>{
    request.delete('/employee/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err =>{})
}

const handleCurrentChange = (rows) =>{
  data.rows = rows
  data.ids =data.rows.map(v =>v.id)
}

const deleteBatch  = () =>{
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的员工')
    return
  }
  ElMessageBox.confirm('您确认删除这些员工吗？删除后无法恢复！','删除确认',{type:"warning"}).then(res =>{
    request.delete('/employee/deleteBatch', { data: data.rows} ).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err =>{})
}

const exportData = () =>{
  let idsStr =data.ids.join(",")
  let url =`http://localhost:9999/employee/export?name=${data.name === null? '' :data.name}`
      +`&departmentId=${data.departmentId === null? '' :data.departmentId}`
      +`&ids=${idsStr}`
      +`&token=${data.user.token}`
  window.open(url);
}

const handleImportSuccess = (res) =>{
  if (res.code === '200'){
    ElMessage.success("批量导入成功")
    load()
  }else {
    ElMessage.error(res.msg)
  }
}

const loadCategory = () =>{
  request.get('/department/selectAll').then(res =>{
    if (res.code === '200') {
      data.categoryData= res.data
    }else {
      ElMessage.error(msg)
    }
  })
}
loadCategory()
</script>
<style>
html {
  overflow-y: scroll;
}
</style>