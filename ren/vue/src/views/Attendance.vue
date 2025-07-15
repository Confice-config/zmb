<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px" >
      <el-input
          clearable
          @click="load"
          style="width: 240px;margin-right: 10px"
          v-model="data.employeeId"
          placeholder="员工ID搜索"
          :prefix-icon="Search"
      ></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="card" style="margin-bottom: 5px" v-if="data.user.roleId !== 3">
      <el-button type="success" @click="handAdd">新增考勤</el-button>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>

    <!-- 考勤表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table
          :data="data.tableData"
          style="width: 100%"
          @selection-change="handleCurrentChange"
          :header-cell-style="{color :'#333', backgroundColor:'#eaf4ff'}"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="考勤ID" />
        <el-table-column prop="employeeName" label="员工姓名" />
        <el-table-column prop="date" label="打卡日期" />
        <el-table-column prop="checkinTime" label="上班时间" />
        <el-table-column prop="checkoutTime" label="下班时间" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" v-if="data.user.roleId !== 3">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)" v-if="data.user.roleId !== 3" />
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)" v-if="data.user.roleId !== 3"/>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total,sizes, prev, pager, next,jumper"
          :total="data.total"
          :page-sizes="[5, 10, 15]"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog title="考勤信息" v-model="data.formVisible" width="35%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="employeeId" label="员工ID">
          <el-input v-model="data.form.employeeId" />
        </el-form-item>

        <el-form-item prop="date" label="打卡日期">
          <el-date-picker
              v-model="data.form.date"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="checkinTime" label="上班时间">
          <el-time-picker
              v-model="data.form.checkinTime"
              placeholder="选择时间"
              format="HH:mm:ss"
              value-format="HH:mm:ss"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="checkoutTime" label="下班时间">
          <el-time-picker
              v-model="data.form.checkoutTime"
              placeholder="选择时间"
              format="HH:mm:ss"
              value-format="HH:mm:ss"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="remark" label="备注">
          <el-input v-model="data.form.remark" type="textarea" rows="3" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  login: JSON.parse(localStorage.getItem('login_user') || "{}"),
  pageNum: 1,         // 当前页
  pageSize: 15,       // 每页数量
  total: 0,           // 总记录数
  tableData: [],      // 表格数据
  formVisible: false, // 对话框显示状态
  form: {},           // 表单数据（新增/编辑）
  rows: [],           // 选中的行（批量删除）
  id:'',
  employeeId:'',
  rules: {            // 表单校验规则（与员工模块类似）
    employeeId: [
      {required: true, message: "请输入员工ID", trigger: "blur"},
      {pattern: /^\d+$/, message: "员工ID必须为数字", trigger: "blur"}
    ],
    date: [
      {required: true, message: "请选择打卡日期", trigger: "change"}
    ],
    checkinTime: [
      {required: true, message: "请选择上班时间", trigger: "blur"}
    ],
    checkoutTime: [
      {required: true, message: "请选择下班时间", trigger: "blur"},
      {
        validator: (rule, value, callback) => {
          if (value < data.form.checkinTime) {
            callback(new Error("下班时间不能早于上班时间"));
          } else {
            callback();
          }
        },
        trigger: "blur"
      }
    ]
  }
});

const formRef = ref(); // 表单引用（用于校验）

// 加载考勤数据
const load = () => {
  request.get("/attendance/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      employeeId:data.employeeId// 搜索关键词（考勤ID或员工ID）
    }
  }).then(res => {
    if (res.code === "200") {
      data.tableData = res.data.list; // 表格数据
      data.total = res.data.total;    // 总记录数
    } else {
      ElMessage.error(res.msg);
    }
  });
};

// 初始化加载数据
 load()

// 重置搜索
const reset = () => {
  data.employeeId = ''
  load();
};

// 新增考勤
const handAdd = () => {
  data.formVisible = true;
  data.form = {}; // 清空表单
};

// 保存（新增/更新）- 关键修改：提交前拼接时间
const save = () => {
  formRef.value.validate(valid => {
    if (!valid) return;

    // 拼接完整时间（核心逻辑）
    const submitForm = {
      ...data.form,  // 复制原表单数据
      checkinTime: `${data.form.date} ${data.form.checkinTime}`,  // 如 "2025-05-23 09:00:00"
      checkoutTime: `${data.form.date} ${data.form.checkoutTime}`
    };

    if (data.form.id) {
      // 更新操作（传递拼接后的时间）
      request.put("/attendance/update", submitForm).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success("更新成功");
          load(); // 刷新数据
        } else {
          ElMessage.error(res.msg);
        }
      });
    } else {
      // 新增操作（传递拼接后的时间）
      request.post("/attendance/add", submitForm).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success("新增成功");
          load(); // 刷新数据
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

// 编辑考勤 - 关键修改：回显时拆分时间
const handleEdit = (row) => {
  // 深拷贝避免响应式问题
  const editRow = JSON.parse(JSON.stringify(row));

  // 拆分后端返回的完整时间（核心逻辑）
  if (editRow.checkinTime) {
    const [date, time] = editRow.checkinTime.split(' ');  // 按空格分割日期和时间
    editRow.date = date;  // 赋值给日期选择器
    editRow.checkinTime = time;  // 赋值给时间选择器
  }
  if (editRow.checkoutTime) {
    editRow.checkoutTime = editRow.checkoutTime.split(' ')[1];  // 直接取时间部分
  }

  data.form = editRow;
  data.formVisible = true;
};

// 删除单条（与员工模块del函数逻辑一致）
const del = (id) => {
  ElMessageBox.confirm("确认删除该考勤记录？删除后无法恢复！", "删除确认", {type: "warning"})
      .then(() => {
        request.delete(`/attendance/delete/${id}`).then(res => {
          if (res.code === "200") {
            ElMessage.success("删除成功");
            load(); // 刷新数据
          } else {
            ElMessage.error(res.msg);
          }
        });
      });
};

// 批量删除（与员工模块deleteBatch逻辑一致）
const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning("请选择要删除的考勤记录");
    return;
  }
  ElMessageBox.confirm("确认删除选中的考勤记录？删除后无法恢复！", "删除确认", {type: "warning"})
      .then(() => {
        request.delete("/attendance/deleteBatch", {data: data.rows}).then(res => {
          if (res.code === "200") {
            ElMessage.success("批量删除成功");
            load(); // 刷新数据
          } else {
            ElMessage.error(res.msg);
          }
        });
      });
};

// 选中行变化
const handleCurrentChange = (rows) => {
  data.rows = rows; // 保存选中的行数据
};
</script>
<style>
html {
  overflow-y: scroll;
}
</style>