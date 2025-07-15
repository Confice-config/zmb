<template>
  <div>
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input
          clearable
          @click="loadLeave"
          style="width: 240px; margin-right: 10px"
          v-model="leaveData.searchText"
          placeholder="按状态/类型搜索"
          :prefix-icon="Search"
      ></el-input>
      <el-button type="primary" @click="loadLeave">搜索</el-button>
      <el-button type="info" @click="resetLeave">重置</el-button>
    </div>

    <!-- 操作栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-button type="success" @click="handAddLeave" v-if="leaveData.user.roleId === 3">提交请假申请</el-button>
      <el-button type="warning" @click="deleteBatchLeave" v-if="leaveData.user.roleId !== 3">批量删除</el-button>
    </div>

    <!-- 表格 -->
    <div class="card">
      <el-table
          :data="leaveData.tableData"
          style="width: 100%"
          @selection-change="handleLeaveCurrentChange"
          :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="请假ID" />
        <el-table-column prop="employeeName" label="员工姓名" />
        <el-table-column prop="leaveType" label="请假类型" />
        <el-table-column prop="startDate" label="开始日期" />
        <el-table-column prop="endDate" label="结束日期" />
        <el-table-column prop="reason" label="请假理由" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" >
          <template v-slot="scope">
            <el-tag type="warning" v-if="scope.row.status === '待审批'">{{ scope.row.status}}</el-tag>
            <el-tag type="success" v-if="scope.row.status === '通过'">{{ scope.row.status}}</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '驳回'">{{ scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" >
          <template #default="scope" v-if="leaveData.user.roleId === 3">
            <el-button
                type="primary"
                icon="Edit"
                circle
                @click="handleEditLeave(scope.row)"
                :disabled="scope.row.status !=='待审批'"
            ></el-button>
            <el-button
                type="danger"
                icon="Delete"
                circle
                @click="delLeave(scope.row.id)"
                :disabled="scope.row.status !=='待审批'"
            ></el-button>
          </template>
          <template #default="scope" v-if="leaveData.user.roleId !== 3">
            <el-button
                type="primary"
                @click="handleEditLeave(scope.row)"
                :disabled="scope.row.status !=='待审批'"
            >审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="card">
      <el-pagination
          v-model:current-page="leaveData.pageNum"
          v-model:page-size="leaveData.pageSize"
          layout="total,sizes, prev, pager, next,jumper"
          :total="leaveData.total"
          :page-sizes="[5, 10, 15]"
          @current-change="loadLeave"
          @size-change="loadLeave"
      />
    </div>

    <!-- 请假表单对话框 -->
    <el-dialog title="请假管理" v-model="leaveData.formVisible" width="35%" destroy-on-close>
      <el-form
          ref="leaveFormRef"
          :model="leaveData.form"
          :rules="leaveData.rules"
          label-width="80px"
          style="padding: 20px 30px 20px 0"
      >
        <el-form-item prop="leaveType" label="请假类型" v-if="leaveData.user.roleId === 3">
          <el-select v-model="leaveData.form.leaveType" placeholder="请选择请假类型">
            <el-option label="年假" value="年假" />
            <el-option label="事假" value="事假" />
            <el-option label="病假" value="病假" />
          </el-select>
        </el-form-item>
        <el-form-item prop="startDate" label="开始日期" v-if="leaveData.user.roleId === 3">
          <el-date-picker
              v-model="leaveData.form.startDate"
              type="date"
              placeholder="Pick a date"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="endDate" label="结束日期" v-if="leaveData.user.roleId === 3">
          <el-date-picker
              v-model="leaveData.form.endDate"
              type="date"
              placeholder="Pick a date"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="reason" label="请假理由" v-if="leaveData.user.roleId === 3">
          <el-input type="textarea" v-model="leaveData.form.reason" :rows="3" />
        </el-form-item>
        <el-form-item prop="reason" label="状态" v-if="leaveData.user.roleId !== 3">
          <el-radio-group v-model="leaveData.form.status" >
            <el-radio-button label="待审批" value="待审批" />
            <el-radio-button label="通过" value="通过" />
            <el-radio-button label="驳回" value="驳回" />
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="leaveData.formVisible = false">取消</el-button>
          <el-button type="primary" @click="saveLeave">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const leaveData = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || "{}"),
  login: JSON.parse(localStorage.getItem('login_user') || "{}"),
  searchText: '',
  pageNum: 1,
  pageSize: 15,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  categoryData:[],
  rules: {
    employeeId: [{ required: true, message: '请填写员工ID', trigger: 'blur' }],
    leaveType: [{ required: true, message: '请填写请假类型', trigger: 'blur' }],
    startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
    endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
    reason: [{ required: true, message: '请填写请假理由', trigger: 'blur' }],
    // status: [{ required: true, message: '请填写状态', trigger: 'blur' }]
  },
  rows: []  // 批量删除选中行
});

const leaveFormRef = ref();

// 加载数据（带搜索）
const loadLeave = () => {
  const searchText = (leaveData.searchText || '').trim();
  let employeeId = '';    // 员工ID（数字）
  let leaveType = '';     // 请假类型（年假/事假/病假）
  let status = '';        // 状态（如"审批中"/"已通过"）

  // 解析顺序：员工ID（数字）→请假类型→状态
  if (/^\d+$/.test(searchText)) {
    employeeId = searchText;  // 匹配员工ID（纯数字）
  } else if (['年假', '事假', '病假'].includes(searchText)) {
    leaveType = searchText;
  } else {
    status = searchText;
  }

  // 输入非空但未匹配任何条件时提示
  if (searchText && !employeeId && !leaveType && !status) {
    ElMessage.warning('请输入有效的员工ID（数字）、类型（年假/事假/病假）或状态');
    return;  // 终止请求
  }
  request.get("/leave/selectPage", {
    params: {
      pageNum: leaveData.pageNum,
      pageSize: leaveData.pageSize,
      employeeId:employeeId,
      leaveType:leaveType,
      status:status,
    }
  }).then(res => {
    if (res.code === '200') {
      leaveData.tableData = res.data.list.map(item => ({
        ...item,
        startDate: item.startDate?.substring(0, 10),
        endDate:   item.endDate?.substring(0, 10)
      }));
      leaveData.total = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};
loadLeave();  // 初始化加载

// 重置搜索
const resetLeave = () => {
  leaveData.searchText = '';
  loadLeave();
};

// 新增请假
const handAddLeave = () => {
  leaveData.formVisible = true;
  leaveData.form = {};  // 清空表单
};

// 编辑请假
const handleEditLeave = (row) => {
  leaveData.form = JSON.parse(JSON.stringify(row));  // 深拷贝避免引用问题
  leaveData.formVisible = true;
};

// 保存（新增/更新）
const saveLeave = () => {
  leaveFormRef.value.validate(valid => {
    if (valid) {
      const api = leaveData.form.id ? "/leave/update" : "/leave/add";
      const method = api.includes("update") ? "put" : "post";

      request[method](api, leaveData.form).then(res => {
        if (res.code === '200') {
          leaveData.formVisible = false;
          ElMessage.success(leaveData.form.id ? '修改成功' : '新增成功');
          loadLeave();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

// 单个删除
const delLeave = (id) => {
  ElMessageBox.confirm('确认删除该请假记录吗？', '删除确认', { type: 'warning' })
      .then(() => {
        request.delete(`/leave/delete/${id}`).then(res => {
          if (res.code === '200') {
            ElMessage.success('删除成功');
            loadLeave();
          } else {
            ElMessage.error(res.msg);
          }
        });
      })
      .catch(() => {});
};

// 批量删除
const deleteBatchLeave = () => {
  if (leaveData.rows.length === 0) {
    ElMessage.warning('请选择要删除的记录');
    return;
  }
  ElMessageBox.confirm('确认删除选中的记录吗？', '删除确认', {type: 'warning'})
      .then(() => {
        request.delete("/leave/deleteBatch", {data: leaveData.rows}).then(res => {
          if (res.code === '200') {
            ElMessage.success('批量删除成功');
            loadLeave();
          } else {
            ElMessage.error(res.msg);
          }
        });
      })
      .catch(() => {
      });
};

// 表格选中事件
const handleLeaveCurrentChange = (rows) => {
  leaveData.rows = rows;
};


const loadCategory = () =>{
  request.get('/leave/selectAll').then(res =>{
    if (res.code === '200') {
      leaveData.categoryData= res.data
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