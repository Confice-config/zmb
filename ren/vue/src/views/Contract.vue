<template>
  <div>
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input
          clearable
          @click="load"
          style="width: 240px;margin-right: 10px"
          v-model="data.searchKey"
          placeholder="输入员工ID/合同类型搜索（如：正式/实习）"
          :prefix-icon="Search"
      ></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="card" style="margin-bottom: 5px" v-if="data.user.roleId !== 3">
      <el-button type="success" @click="handAdd">新增合同</el-button>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>

    <!-- 合同表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table
          :data="data.tableData"
          style="width: 100%"
          @selection-change="handleCurrentChange"
          :header-cell-style="{color :'#333', backgroundColor:'#eaf4ff'}"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="合同ID" />
        <el-table-column prop="employeeName" label="员工姓名" />
        <el-table-column prop="contractType" label="合同类型" />
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
        <el-table-column prop="status" label="状态" />
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
    <el-dialog title="合同信息" v-model="data.formVisible" width="35%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="employeeId" label="员工ID">
          <el-input v-model="data.form.employeeId" />
        </el-form-item>

        <el-form-item prop="contractType" label="合同类型">
          <el-select v-model="data.form.contractType" placeholder="请选择合同类型">
            <el-option label="正式" value="正式" />
            <el-option label="实习" value="实习" />
            <el-option label="临时" value="临时" />
          </el-select>
        </el-form-item>

        <el-form-item prop="startDate" label="开始日期">
          <el-date-picker
              v-model="data.form.startDate"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="endDate" label="结束日期">
          <el-date-picker
              v-model="data.form.endDate"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="status" label="合同状态">
          <el-select v-model="data.form.status" placeholder="请选择状态">
            <el-option label="有效" value="有效" />
            <el-option label="已过期" value="已过期" />
            <el-option label="终止" value="终止" />
          </el-select>
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
  searchKey: '',
  employeeId:'',
  status:'',
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
    contractType: [
      { required: true, message: "请选择合同类型", trigger: "change" }
    ],
    startDate: [
      { required: true, message: "请选择开始日期", trigger: "change" }
    ],
    endDate: [
      { required: true, message: "请选择结束日期", trigger: "change" },
      {
        validator: (_, value, cb) => {
          if (value < data.form.startDate) {
            cb(new Error("结束日期不能早于开始日期"));
          } else {
            cb();
          }
        },
        trigger: "change"
      }
    ],
    status: [
      { required: true, message: "请选择合同状态", trigger: "change" }
    ]
  }
});

const formRef = ref();

// 加载数据
const load = () => {

  const searchKey = data.searchKey.trim();  // 获取用户输入的搜索内容
  let employeeId = '';
  let contractType = '';
  let status = '';

  if (/^\d+$/.test(searchKey)) {
    employeeId = searchKey;  // 匹配员工ID（数字）
  } else if (['实习', '正式', '临时'].includes(searchKey)) {  // 匹配合同类型
    contractType = searchKey;
  } else if (['有效', '已过期', '终止'].includes(searchKey)) {  // 匹配状态
    status = searchKey;
  } else if (searchKey) {  // 输入不匹配任何条件
    ElMessage.warning('请输入有效的员工ID（数字）、合同类型（实习/正式/临时）或状态（有效/已过期/终止）');
    return;  // 终止请求
  }
  request.get("/contract/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      employeeId:employeeId,
      status:status,
      contractType:contractType,
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

// 重置搜索
const reset = () => {
  data.searchText = "";
  load();
};

// 新增合同
const handAdd = () => {
  data.formVisible = true;
  data.form = {
    startDate: new Date().toISOString().split("T")[0],  // 默认当前日期
    endDate: new Date(new Date().getTime() + 365 * 24 * 3600 * 1000).toISOString().split("T")[0]  // 默认1年后
  };
};

// 保存（新增/更新）
const save = () => {
  formRef.value.validate(valid => {
    if (!valid) return;
    if (data.form.id) {
      request.put("/contract/update", data.form).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success("更新成功");
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    } else {
      request.post("/contract/add", data.form).then(res => {
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

// 编辑合同
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

// 删除单条
const del = (id) => {
  ElMessageBox.confirm("确认删除该合同记录？", "删除确认", { type: "warning" })
      .then(() => {
        request.delete(`/contract/delete/${id}`).then(res => {
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
    ElMessage.warning("请选择要删除的合同记录");
    return;
  }
  ElMessageBox.confirm("确认删除选中的合同记录？", "删除确认", { type: "warning" })
      .then(() => {
        const ids = data.rows.map(row => row.id);
        request.delete("/contract/deleteBatch", { data: ids }).then(res => {
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