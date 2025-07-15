<template>
  <div>
    <!-- 搜索栏 -->
    <div class="card" style="margin-bottom: 5px">
      <el-input
          clearable
          @click="load"
          style="width: 240px;margin-right: 10px"
          v-model="data.searchText"
          placeholder="输入培训名称/日期搜索（如：新员工培训）"
          :prefix-icon="Search"
      ></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <!-- 操作按钮 -->
    <div class="card" style="margin-bottom: 5px" v-if="data.user.roleId !== 3">
      <el-button type="success" @click="handAdd">新增培训</el-button>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>

    <!-- 培训表格 -->
    <div class="card" style="margin-bottom: 5px">
      <el-table
          :data="data.tableData"
          style="width: 100%"
          @selection-change="handleCurrentChange"
          :header-cell-style="{color :'#333', backgroundColor:'#eaf4ff'}"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="培训ID" />
        <el-table-column prop="title" label="培训名称" />
        <el-table-column prop="date" label="培训日期" />
        <el-table-column prop="description" label="培训内容" :show-overflow-tooltip="true" />
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
    <el-dialog title="培训信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="title" label="培训名称">
          <el-input v-model="data.form.title" placeholder="请输入培训名称" />
        </el-form-item>

        <el-form-item prop="date" label="培训日期">
          <el-date-picker
              v-model="data.form.date"
              type="date"
              placeholder="选择日期"
              value-format="YYYY-MM-DD"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="description" label="培训内容">
          <el-input
              v-model="data.form.description"
              type="textarea"
              rows="4"
              placeholder="请输入培训详细内容"
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
  searchText: "",
  pageNum: 1,
  pageSize: 15,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rows: [],
  rules: {
    title: [
      { required: true, message: "请输入培训名称", trigger: "blur" },
      { max: 100, message: "培训名称最多100字符", trigger: "blur" }
    ],
    date: [
      { required: true, message: "请选择培训日期", trigger: "change" }
    ],
    description: [
      { required: true, message: "请输入培训内容", trigger: "blur" }
    ]
  }
});

const formRef = ref();

// 加载数据
const load = () => {
  request.get("/training/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      searchText: data.searchText
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

load()

// 重置搜索
const reset = () => {
  data.searchText = "";
  load();
};

// 新增培训
const handAdd = () => {
  data.formVisible = true;
  data.form = {
    date: new Date().toISOString().split("T")[0]  // 默认当前日期
  };
};

// 保存（新增/更新）
const save = () => {
  formRef.value.validate(valid => {
    if (!valid) return;
    if (data.form.id) {
      request.put("/training/update", data.form).then(res => {
        if (res.code === "200") {
          data.formVisible = false;
          ElMessage.success("更新成功");
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    } else {
      request.post("/training/add", data.form).then(res => {
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

// 编辑培训
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
};

// 删除单条
const del = (id) => {
  ElMessageBox.confirm("确认删除该培训记录？", "删除确认", { type: "warning" })
      .then(() => {
        request.delete(`/training/delete/${id}`).then(res => {
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
    ElMessage.warning("请选择要删除的培训记录");
    return;
  }
  ElMessageBox.confirm("确认删除选中的培训记录？", "删除确认", { type: "warning" })
      .then(() => {
        const ids = data.rows.map(row => row.id);
        request.delete("/training/deleteBatch", { data: ids }).then(res => {
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