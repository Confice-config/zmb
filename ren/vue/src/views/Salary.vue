<template>
  <div>
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px" >
      <el-input
          clearable
          @click="load"
          style="width: 240px;margin-right: 10px"
          v-model="data.month"
          placeholder="输入员工ID/月份搜索（格式：2025-05）"
          :prefix-icon="Search"
      ></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="card" style="margin-bottom: 5px" v-if="data.user.roleId !== 3"  >
      <el-button type="success" @click="handAdd">新增薪资</el-button>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>

    <!-- 薪资表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table
          :data="data.tableData"
          style="width: 100%"
          @selection-change="handleCurrentChange"
          :header-cell-style="{color :'#333', backgroundColor:'#eaf4ff'}"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="薪资ID" />
        <el-table-column prop="employeeName" label="员工姓名" />
        <el-table-column prop="month" label="发薪月份" />
        <el-table-column prop="baseSalary" label="基本工资" />
        <el-table-column prop="bonus" label="奖金" />
        <el-table-column prop="performance" label="绩效工资" />
        <el-table-column label="操作" v-if="data.user.roleId !== 3">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)" />
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)" />
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
    <el-dialog title="薪资信息" v-model="data.formVisible" width="35%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="employeeId" label="员工ID">
          <el-input v-model="data.form.employeeId" />
        </el-form-item>

        <el-form-item prop="month" label="发薪月份">
          <el-input
              v-model="data.form.month"
              placeholder="格式：2025-05"
          />
        </el-form-item>

        <el-form-item prop="baseSalary" label="基本工资">
          <el-input
              v-model.number="data.form.baseSalary"
              placeholder="请输入数字（保留2位小数）"
          />
        </el-form-item>

        <el-form-item prop="bonus" label="奖金">
          <el-input
              v-model.number="data.form.bonus"
              placeholder="请输入数字（保留2位小数）"
          />
        </el-form-item>

        <el-form-item prop="performance" label="绩效工资">
          <el-input
              v-model.number="data.form.performance"
              placeholder="请输入数字（保留2位小数）"
          />
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
import { reactive, ref, onMounted } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  login: JSON.parse(localStorage.getItem('login_user') || "{}"),
  employeeId: '',
  month:'',
  pageNum: 1,
  pageSize: 15,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rows: [],
  rules: {
    employeeId: [
      { required: true, message: "请输入员工ID", trigger: "blur" },
      { pattern: /^\d+$/, message: "员工ID必须为数字", trigger: "blur" }
    ],
    month: [
      { required: true, message: "请输入发薪月份", trigger: "blur" },
      { pattern: /^\d{4}-\d{2}$/, message: "月份格式应为YYYY-MM", trigger: "blur" }
    ],
    baseSalary: [
      { required: true, message: "请输入基本工资", trigger: "blur" },
      { validator: (_, value, cb) => value >= 0 || cb("金额不能为负数"), trigger: "blur" }
    ],
    bonus: [
      { required: true, message: "请输入奖金", trigger: "blur" },
      { validator: (_, value, cb) => value >= 0 || cb("金额不能为负数"), trigger: "blur" }
    ],
    performance: [
      { required: true, message: "请输入绩效工资", trigger: "blur" },
      { validator: (_, value, cb) => value >= 0 || cb("金额不能为负数"), trigger: "blur" }
    ]
  }
});

const formRef = ref();

// 加载数据
const load = () => {
  const inputValue = data.month.trim();  // 获取输入框内容（原绑定的month字段）
  let employeeId = '';
  let month = '';
  if (/^\d+$/.test(inputValue)) {       // 匹配纯数字（员工ID）
    employeeId = inputValue;
  } else if (/^\d{4}-\d{2}$/.test(inputValue)) {  // 匹配月份格式（YYYY-MM）
    month = inputValue;
  } else if (inputValue) {  // 输入内容既不是数字也不是月份格式
    ElMessage.warning('请输入有效的员工ID（纯数字）或月份（格式：YYYY-MM）');
    return;  // 终止请求
  }
  request.get("/salary/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      employeeId:employeeId,
      month:month,
    }
  }).then(res => {
    if (res.code === "200") {
      data.tableData = res.data.list;
      data.total = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};

onMounted(() => load());

// 重置
const reset = () => {
  data.month = "";
  load();
};

// 新增
const handAdd = () => {
  data.formVisible = true;
  data.form = {
    baseSalary: 0,
    bonus: 0,
    performance: 0
  };
};

// 保存
const save = () => {
  formRef.value.validate(valid => {
    if (!valid) return;
    if (data.form.id) {
      request.put("/salary/update", data.form).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success("更新成功");
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    } else {
      request.post("/salary/add", data.form).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success("新增成功");
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

// 删除单条
const del = (id) => {
  ElMessageBox.confirm("确认删除该薪资记录？", "删除确认", { type: "warning" })
      .then(() => {
        request.delete(`/salary/delete/${id}`).then(res => {
          if (res.code === "200") {
            ElMessage.success("删除成功");
            load();
          } else {
            ElMessage.error(res.msg);
          }
        });
      });
};

// 批量删除
const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning("请选择要删除的记录");
    return;
  }
  ElMessageBox.confirm("确认删除选中的薪资记录？", "删除确认", { type: "warning" })
      .then(() => {
        const ids = data.rows.map(row => row.id);
        request.delete("/salary/deleteBatch", { data: ids }).then(res => {
          if (res.code === "200") {
            ElMessage.success("批量删除成功");
            load();
          } else {
            ElMessage.error(res.msg);
          }
        });
      });
};

// 选中行变化
const handleCurrentChange = (rows) => {
  data.rows = rows;
};

</script>
<style>
html {
  overflow-y: scroll;
}
</style>