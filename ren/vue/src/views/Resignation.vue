<template>
  <div style="font-family: Arial, sans-serif">
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input
          clearable
          @click="load"
          v-model="data.search"
          placeholder="按状态/类型搜索"
          :prefix-icon="Search"
          style="width: 240px; margin-right: 10px"
      />
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 操作栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-button type="success" @click="openAdd" v-if="data.user.roleId === 3">新增离职申请</el-button>
      <el-button type="warning" @click="delAll" v-if="data.user.roleId !== 3">批量删除</el-button>
    </div>

    <!-- 表格 -->
    <el-table
        :data="data.list"
        @selection-change="selChange"
        style="width: 100%; text-align: center"
        :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff', textAlign: 'center' }"
        :cell-style="{ textAlign: 'center' }"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="employeeName" label="员工ID" />
      <el-table-column prop="resignationType" label="类型" />
      <el-table-column prop="applyDate" label="申请日期" />
      <el-table-column prop="lastWorkDate" label="最后工作日" />
      <el-table-column prop="reason" label="理由" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" >
        <template v-slot="scope">
          <el-tag type="warning" v-if="scope.row.status === '待审批'">{{ scope.row.status}}</el-tag>
          <el-tag type="success" v-if="scope.row.status === '已批准'">{{ scope.row.status}}</el-tag>
          <el-tag type="danger" v-if="scope.row.status === '已驳回'">{{ scope.row.status}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="departmentName" label="审批人" />
      <el-table-column prop="approvalComment" label="审批意见" show-overflow-tooltip />
      <el-table-column label="操作" width="100" >
        <template #default="scope" v-if="data.user.roleId === 3">
          <el-button type="primary" icon="Edit" circle @click="openEdit(scope.row)" :disabled="scope.row.status !=='待审批'"/>
          <el-button type="danger" icon="Delete" circle @click="delOne(scope.row.id)" :disabled="scope.row.status !=='待审批'"/>
        </template>
        <template #default="scope" v-if="data.user.roleId !== 3">
          <el-button type="primary"  @click="openEdit(scope.row)" :disabled="scope.row.status !=='待审批'">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="card" style="margin-top: 5px">
      <el-pagination
          v-model:current-page="data.page"
          v-model:page-size="data.size"
          :total="data.total"
          :page-sizes="[5, 10, 15]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <!-- 表单 -->
    <el-dialog title="离职管理" v-model="data.show" width="40%" destroy-on-close>
      <el-form
          ref="formRef"
          :model="data.form"
          :rules="data.rules"
          label-width="100px"
          style="padding: 20px 30px"
      >
        <el-form-item prop="resignationType" label="类型" v-if="data.user.roleId === 3">
          <el-select v-model="data.form.resignationType" placeholder="请选择">
            <el-option label="主动离职" value="主动离职" />
            <el-option label="辞退" value="辞退" />
            <el-option label="退休" value="退休" />
          </el-select>
        </el-form-item>
        <el-form-item prop="applyDate" label="申请日期" v-if="data.user.roleId === 3">
          <el-date-picker
              v-model="data.form.applyDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="lastWorkDate" label="最后工作日" v-if="data.user.roleId === 3">
          <el-date-picker
              v-model="data.form.lastWorkDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="reason" label="理由" v-if="data.user.roleId === 3">
          <el-input type="textarea" v-model="data.form.reason" :rows="3" />
        </el-form-item>
        <el-form-item prop="status" label="状态" v-if="data.user.roleId !== 3">
          <el-select v-model="data.form.status" placeholder="请选择">
            <el-option label="待审批" value="待审批" />
            <el-option label="已批准" value="已批准" />
            <el-option label="已驳回" value="已驳回" />
          </el-select>
        </el-form-item>
        <el-form-item prop="approverId" label="审批人ID" v-if="data.user.roleId !== 3">
          <el-input v-model="data.form.approverId" />
        </el-form-item>
        <el-form-item prop="approvalComment" label="审批意见" v-if="data.user.roleId !== 3">
          <el-input type="textarea" v-model="data.form.approvalComment" :rows="3" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.show = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from "@/utils/request";

const formRef = ref();

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  login: JSON.parse(localStorage.getItem('login_user') || "{}"),
  search: '',
  page: 1,
  size: 15,
  total: 0,
  list: [],
  show: false,
  form: {},
  rules: {
    employeeId: [{ required: true, message: "请填写员工ID", trigger: "blur" }],
    resignationType: [{ required: true, message: "请选择类型", trigger: "change" }],
    applyDate: [{ required: true, message: "请选择日期", trigger: "change" }],
    lastWorkDate: [{ required: true, message: "请选择最后工作日", trigger: "change" }],
    reason: [{ required: true, message: "请填写理由", trigger: "blur" }],
    status: [{ required: true, message: "请选择状态", trigger: "change" }],
    approverId:[{ required: true, message: "请填写审核人id" }]
  },
  rows: []
});

const load = () => {
  const searchText = (data.search || '').trim();
  let employeeId = '';       // 员工ID（数字）
  let resignationType = '';  // 离职类型（枚举值）
  let status = '';           // 状态（枚举值）

  // 解析顺序：数字→离职类型→状态
  if (/^\d+$/.test(searchText)) {
    employeeId = searchText;  // 匹配数字（员工ID）
  } else if (['主动离职', '辞退', '退休'].includes(searchText)) {
    resignationType = searchText;  // 匹配离职类型枚举值
  } else if (['待审批', '已批准', '已驳回'].includes(searchText)) {
    status = searchText;  // 匹配状态枚举值
  } else if (searchText) {
    // 输入不匹配任何条件时提示
    ElMessage.warning('请输入有效的员工ID（数字）、类型（主动离职/辞退/退休）或状态（待审批/已批准/已驳回）');
    return;
  }

  request.get("/resign/selectPage", {
    params: {
      pageNum: data.page,
      pageSize: data.size,
      employeeId: employeeId,
      resignationType: resignationType,
      status: status
    }
  }).then(res => {
    if (res.code === '200') {
      data.list = res.data.list;
      data.total = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};
load();

const reset = () => {
  data.search = '';
  load();
};

const openAdd = () => {
  data.form = {};
  data.show = true;
};

const openEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.show = true;
};

const save = () => {
  formRef.value.validate(valid => {
    if (!valid) return;
    const api = data.form.id ? "/resign/update" : "/resign/add";
    const method = api.includes("update") ? "put" : "post";

    request[method](api, data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success(data.form.id ? "修改成功" : "新增成功");
        data.show = false;
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  });
};

const delOne = (id) => {
  ElMessageBox.confirm("确认删除该记录吗？", "提示", { type: "warning" })
      .then(() => {
        request.delete(`/resign/delete/${id}`).then(res => {
          if (res.code === '200') {
            ElMessage.success("删除成功");
            load();
          } else {
            ElMessage.error(res.msg);
          }
        });
      })
      .catch(() => {
      });
};

const selChange = (rows) => {
  data.rows = rows;
};

const delAll = () => {
  if (data.rows.length === 0) {
    ElMessage.warning("请先选择记录");
    return;
  }
  ElMessageBox.confirm("确认批量删除选中记录？", "提示", {type: "warning"})
      .then(() => {
        request.delete("/resign/deleteBatch", {data: data.rows}).then(res => {
          if (res.code === '200') {
            ElMessage.success("删除成功");
            load()
          } else {
            ElMessage.error(res.msg);
          }
        });
      })
      .catch(() => {
      });
};
</script>
<style>
html {
  overflow-y: scroll;
}
</style>