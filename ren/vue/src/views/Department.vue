<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @click="load" style="width: 240px;margin-right: 10px" v-model="data.searchText" placeholder="请输入部门名称/负责人" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="success" @click="handAdd">新增部门</el-button>
      <el-button type="warning" @click="deleteBatch">批量删除</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleCurrentChange" :header-cell-style="{color :'#333',backgroundColor:'#eaf4ff'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="id" label="部门ID" />
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="manager" label="部门负责人" />
        <el-table-column prop="description" label="部门描述" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
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

    <el-dialog title="部门信息" v-model="data.formVisible" width="35%" destroy-on-close>
      <el-form ref="fromRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 20px 0">
        <el-form-item prop="name" label="部门名称">
          <el-input v-model="data.form.name" />
        </el-form-item>
        <el-form-item prop="manager" label="负责人">
          <el-input v-model="data.form.manager" />
        </el-form-item>
        <el-form-item prop="description" label="部门描述">
          <el-input type="textarea" v-model="data.form.description" :rows="3" />
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
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  searchText: '',
  pageNum: 1,
  pageSize: 15,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    name: [
      { required: true, message: '请输入部门名称', trigger: 'blur' },
      { max: 100, message: '部门名称长度不能超过100字', trigger: 'blur' }
    ],
    manager: [
      { max: 50, message: '负责人姓名长度不能超过50字', trigger: 'blur' }
    ]
  },
  rows: []
});

const fromRef = ref();

// 加载数据
const load = () => {
  request.get("/department/selectPage", {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      searchText: data.searchText
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list;
      data.total = res.data.total;
    } else {
      ElMessage.error(res.msg);
    }
  });
};
load();

// 重置搜索
const reset = () => {
  data.searchText = '';
  load();
};

// 新增部门
const handAdd = () => {
  data.formVisible = true;
  data.form = {}; // 初始化空表单
};

// 保存（新增/更新）
const save = () => {
  data.form.id ? update() : add();
};

// 新增提交
const add = () => {
  fromRef.value.validate(valid => {
    if (valid) {
      request.post("/department/add", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false;
          ElMessage.success('部门新增成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

// 编辑回显
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row)); // 深拷贝避免引用问题
  data.formVisible = true;
};

// 更新提交
const update = () => {
  fromRef.value.validate(valid => {
    if (valid) {
      request.put("/department/update", data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false;
          ElMessage.success('部门修改成功');
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

// 单个删除
const del = (id) => {
  ElMessageBox.confirm('确认删除该部门？删除后关联的员工数据也将被级联删除！', '删除确认', { type: "warning" })
      .then(() => {
        request.delete(`/department/delete/${id}`).then(res => {
          if (res.code === '200') {
            ElMessage.success('部门删除成功');
            load();
          } else {
            ElMessage.error(res.msg);
          }
        });
      })
      .catch(() => {});
};

// 批量删除
const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的部门');
    return;
  }
  ElMessageBox.confirm(`确认批量删除${data.rows.length}个部门？删除后关联数据将被级联删除！`, '批量删除确认', { type: "warning" })
      .then(() => {
        request.delete("/department/deleteBatch", { data: data.rows })
            .then(res => {
              if (res.code === '200') {
                ElMessage.success('批量删除成功');
                load();
              } else {
                ElMessage.error(res.msg);
              }
            });
      })
      .catch(() => {});
};


const handleCurrentChange = (rows) => {
  data.rows = rows;
};
</script>
<style>
html {
  overflow-y: scroll;
}
</style>
