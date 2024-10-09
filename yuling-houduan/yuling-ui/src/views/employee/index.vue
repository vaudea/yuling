<template>
  <div class="app-container" width="100%">
    <!-- 搜索表单 -->
    <div class="form-container" style="margin-left: -10px;margin-top: -8px ;"> <!-- 左移表单 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
        <el-form-item label="员工工号" prop="jobNumber">
          <el-input v-model="queryParams.jobNumber" placeholder="请输入员工工号" clearable @keyup.enter.native="getList" />
        </el-form-item>
        <el-form-item label="员工姓名" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入员工姓名" clearable @keyup.enter.native="getList" />
        </el-form-item>
        <el-form-item label="性别" prop="gander">
          <el-select v-model="queryParams.gender" placeholder="请选择性别">
            <el-option label="男" value="0"></el-option>
            <el-option label="女" value="1"></el-option>
          </el-select>
        </el-form-item>
        <!-- 部门下拉框 -->
        <el-form-item label="所属部门" prop="departmentId" v-if="user.fatherId == 1 || user.positionId >= 5">
          <el-select v-model="queryParams.departmentId" placeholder="请选择所属部门">
            <el-option label="人力资源部" value="1" key="1" />
            <el-option label="财务部" value="2" key="2" />
            <el-option label="技术部" value="3" key="3" />
            <el-option label="市场部" value="4" key="4" />
            <el-option label="销售部" value="5" key="5" />
            <el-option label="客服部" value="6" key="6" />
            <el-option label="研发部" value="7" key="7" />
            <el-option label="行政部" value="8" key="8" />

          </el-select>
        </el-form-item>
        <!-- 职位下拉框 -->
        <el-form-item label="职位" prop="positionId" v-if="user.fatherId == 1 || user.positionId >= 5">
          <el-select v-model="queryParams.positionId" placeholder="请选择职位">
            <el-option v-for="list in position" :label="list.positionName" :value="list.positionId"
              :key="list.positionId" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="findBySearch">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5" v-if="(user.positionId >= 2 && user.fatherId == 1) || (user.positionId >= 5)">
        <el-button  type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5" v-if="user.positionId >= 3">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5" v-if="(user.positionId >= 4 && user.fatherId == 1) || (user.positionId >= 5)">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5" v-if="(user.positionId >= 2 && user.fatherId == 1) || (user.positionId >= 5)">
        <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>
    <!-- 员工表格 -->
    <el-table v-loading="loading" :data="list" @selection-change="employeeSelectionChange" width="100%"
      :row-style="{ height: '45px' }" :cell-style="{ padding: '0' }">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工工号" align="center" prop="jobNumber" width="106" />
      <el-table-column label="员工姓名" align="center" prop="name" />
      <el-table-column label="性别" align="center">
        <template slot-scope="scope">
          {{ getGenderText(scope.row.gender) }}
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center" prop="phone" width="106" />
      <el-table-column label="所属部门" align="center" prop="departmentName" />
      <el-table-column label="所在职位" align="center" prop="positionName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="190">
      </el-table-column>
      <el-table-column label="员工状态" align="center" v-if="user.positionId >= 3">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.active" :active-value="0" :inactive-value="1" active-color="#71c9ce"
            @change="handleActive(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="银行卡号" align="center" prop="bankCard" width="145" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150"
        v-if="user.positionId >= 3">
        <template slot-scope="scope">
          <el-button size="mini" type="success" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-if="user.positionId >= 3">修改</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-if="(user.positionId >= 4 && user.departmentId == 1) || (user.positionId >= 5)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="margin-top: 1px; width: auto;text-align: center;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum" :page-sizes="[10, 16, 30, 40]" :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <!-- 添加或修改员工对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入员工姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%;">
            <el-option label="男" value="0" />
            <el-option label="女" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthdate">
          <el-date-picker clearable v-model="form.birthdate" type="date" value-format="yyyy-MM-dd" placeholder="请选择出生日期"
            style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="form.email" placeholder="请输入电子邮件" />
        </el-form-item>
        <!-- 所属部门 -->
        <el-form-item label="所属部门" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择所属部门" style="width: 100%;">
            <el-option v-for="list in department" :label="list.departmentName" :value="list.departmentId"
              :key="list.departmentId" />
          </el-select>
        </el-form-item>
        <!-- 职位 -->
        <el-form-item label="职位" prop="positionId">
          <el-select v-model="form.positionId" placeholder="请选择职位" style="width: 100%;">
            <el-option v-for="list in position" :label="list.positionName" :value="list.positionId"
              :key="list.positionId" />
          </el-select>
        </el-form-item>
        <el-form-item label="银行卡号" prop="bankCard">
          <el-input v-model="form.bankCard" placeholder="请输入银行卡号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <!-- :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" -->
      <el-upload action="#" ref="upload" :limit="1" accept=".xlsx, .xls" :disabled="upload.isUploading"
        :auto-upload="false" drag :http-request="customUploadHandler">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="uploadClose">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import request from '@/utils/request';
import _ from 'lodash';
export default {
  data() {
    const users = JSON.parse(this.$cookies.get('user'));
    return {
      user: users,
      // 遮罩层，控制加载状态
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用，当未选中或选中多于一个时，单个操作按钮禁用
      single: true,
      // 非多个禁用，当未选中时，多个操作按钮禁用
      multiple: true,
      // 总条数
      total: 0,
      // 员工表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1, // 当前页码
        pageSize: 10, // 每页显示条数
        jobNumber: null, // 员工工号
        name: null, // 员工姓名
        gender: null, // 性别
        departmentId: null, // 部门ID
        positionId: null, // 职位ID
        fatherId: null,
        userJobNumber: null,
        userPositionId: null,
        userDepartmentId: null,
        userDepartmentFatherId: null,
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {},
        // 上传的地址
        url: ''
      },
      // 表单参数
      form: {},
      // 表单校验规则
      rules: {
        name: [
          { required: true, message: "员工姓名不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        birthdate: [
          { required: true, message: "出生日期不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" },
          { pattern: /^[0-9]{11}$/, message: '联系电话必须为11位数字', trigger: 'blur' }
        ],
        bankCard: [
          { required: true, message: "银行卡号不能为空", trigger: "blur" },
          { pattern: /^[0-9]{16,19}$/, message: "银行卡号必须为16到19位数字", trigger: "blur" }
        ],
        email: [
          { required: true, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '邮箱格式不正确', trigger: ['blur', 'change'] }
        ],
      },
      position: {},
      department: {}
    };
  },
  created() {
    this.getList();
    this.getOption();

  },
  computed: {
  },
  methods: {
    // 下拉框获取
    getOption() {
      let positionId = this.user.positionId;
      let fatherId = this.user.fatherId;

      if (positionId >= 5 || fatherId == 1) {
        fatherId = 0;
      }
      request.get(`/employee/position?positionId=${positionId}`).then((res) => {
        if (res.code == 200) {
          this.position = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
      request.get(`/employee/department?fatherId=${fatherId}`).then((res) => {
        if (res.code == 200) {
          this.department = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 获取员工列表数据
    getList() {
      this.loading = true;
      if (this.user.positionId >= 3 || this.user.fatherId == 1) {
        if (this.user.fatherId != 1 && this.user.positionId < 5) {
          this.queryParams.userDepartmentFatherId = this.user.fatherId;
          this.queryParams.userPositionId = this.user.positionId;
        }
        this.queryParams.userJobNumber = this.user.jobNumber;

        request.get(`/employee/employeeList`, { params: this.queryParams }).then((res) => {
          if (res.code == 200) {
            this.list = res.data.list;
            this.total = res.data.total;
            this.loading = false;
          } else {
            this.loading = false;
            this.$message.error(res.msg);
            this.total = 0;
          }
        });
      } else {
        this.$model.msgError("您没有权限查看员工信息");
        this.loading = false;
        this.$router.push('/');
      }

    },
    //搜索按钮操作
    findBySearch() {
      this.getList();
    },
    // 重置查询参数
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        jobNumber: null, // 员工工号
        name: null, // 员工姓名
        gender: null, // 性别
        departmentId: null, // 部门ID
        positionId: null, // 职位ID
        fatherId: null,
        fatherId: null,
        userJobNumber: null,
        userPositionId: null,
        userDepartmentId: null,
        userDepartmentFatherId: null,
      };
      this.getList();
    },
    // 取消对话框
    cancel() {
      this.reset();
      this.$refs['form'].resetFields(); // 重置表单验证状态
      this.open = false;
    },
    // 重置表单
    reset() {
      this.form = {};
      this.queryParams = {
        pageNum: 1, // 当前页码
        pageSize: 10, // 每页显示条数
        jobNumber: null, // 员工工号
        name: null, // 员工姓名
        gender: null, // 性别
        departmentId: null, // 部门ID
        positionId: null, // 职位ID
      }
    },
    // 新增按钮操作
    handleAdd() {
      if ((this.user.positionId >= 2 && this.user.fatherId == 1) || (this.user.positionId >= 5)) {
        this.reset();
        this.open = true;
        if (this.$refs['form']) {
          this.$refs['form'].resetFields();// 重置表单验证状态
        }
        this.title = "添加员工";
      } else {
        this.$model.msgError("您没有权限添加员工信息");
      }

    },
    // 修改按钮操作
    handleUpdate(row) {
      if (this.user.positionId >= 3) {
        if (this.user.positionId <= row.positionId) {
          this.$model.msgError("您不能修改同等职位或更高职位的员工信息");
          return;
        }
        this.reset();
        const jobNumbers = row.jobNumber === 0 ? row.jobNumber : (row.jobNumber || this.ids);
        request.get(`/employee/getEmployee?jobNumber=${jobNumbers}`).then((res) => {
          this.reset();
          if (res.code == "200") {
            this.form = res.data;
            this.open = true;
            // 确保form引用已经可用，延迟调用resetFields
            if (this.$refs['form']) {
              this.$refs['form'].resetFields();
            }
            this.title = "修改员工";
          } else {
            this.$message.error("获取员工信息失败");
          }
        })
      } else {
        this.$model.msgError("您没有权限修改员工信息");
      }
    },
    // 提交表单
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.jobNumber != null) {
            if (this.user.positionId < 3) {
              this.$model.msgError("您没有权限修改员工信息");
              return;
            }
            if (this.user.positionId <= this.form.positionId) {
              this.$model.msgError("您不能修改同等职位或更高职位的员工信息");
              return;
            }
            this.submitForm = this.$throttle(this.submitForm, 2000, "数据已经提交请勿重复提交");
            this.form.userJobNumber = this.user.jobNumber;
            this.form.userPositionId = this.user.positionId;
            this.form.userDepartmentFatherId = this.user.fatherId;
            request.put("/employee/employeeUpdate", this.form).then((res) => {
              if (res.code == 200) {
                this.$message.success("修改成功");
                this.open = false;
              } else {
                this.$message.error(res.msg);
              }
            });
          } else {
            if ((this.user.positionId >= 2 && this.user.fatherId == 1) || (this.user.positionId <= 5)) {
              this.$model.msgError("您没有权限添加员工信息");
              return;
            }
            if (this.user.positionId <= this.form.positionId) {
              this.$model.msgError("您不能添加同等职位或更高职位的员工信息");
              return;
            }
            this.submitForm = this.$throttle(this.submitForm, 2000, "数据已经提交请勿重复提交");
            this.form.userJobNumber = this.user.jobNumber;
            this.form.userPositionId = this.user.positionId;
            this.form.userDepartmentFatherId = this.user.fatherId;
            request.post("/employee/employeeInsert", this.form).then((res) => {
              if (res.code == 200) {
                this.$message.success("新增成功");
                this.open = false;
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
      this.getList();

    },
    // 删除员工
    handleDelete(row) {
      if ((this.user.positionId >= 4 && this.user.fatherId == 1) || (this.user.positionId <= 5)) {
        this.$model.msgError("您没有权限删除员工信息");
        return;
      }
      if (this.user.positionId <= row.positionId) {
        this.$model.msgError("您没有权限删除同等职位或更高职位的员工账户");
        return;
      }
      const jobNumbers = row.jobNumber === 0 ? row.jobNumber : (row.jobNumber || this.ids);
      this.$model.confirm('确认要删除工号为 ' + jobNumbers + ' 员工吗?').then(() => {
        return request.delete('/employee/employeeDelete/' + jobNumbers);
      }).then((res) => {
        if (res.code == 200) {
          this.$message.success("删除成功");
          this.getList();
        } else {
          this.$model.msgError(res.msg);
        }
      }).catch((err) => {
        if (err !== 'cancel') { // 过滤掉用户取消操作
          this.$model.msgError("删除失败: " + err.message);
        }
      });
    },

    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    //导入数据
    customUploadHandler(option) {
      if ((this.user.positionId >= 2 && this.user.fatherId == 1) || (this.user.positionId >= 5)) {
        const formData = new FormData(); // 创建 FormData 对象来封装文件数据
        formData.append('file', option.file); // 将文件添加到 FormData 对象
        // 发起 POST 请求上传文件
        request.post('/employee/upload', formData).then((res) => {
          if (res.code == 200) {
          } else {
          }
        }).catch(error => {
          this.$model.msgError(error.message); // 显示错误消息
        });
      } else {
        this.$model.msgError("您没有权限导入员工信息");
      }
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles();
      }
    },
    // 取消导入
    uploadClose() {
      this.$refs.upload.clearFiles();
      this.upload.open = false;
    },
    // 提交上传文件
    submitFileForm() {
      const uploadFiles = this.$refs.upload.uploadFiles;
      if (uploadFiles.length == 1) {
        this.submitFileForm = this.$throttle(this.submitFileForm, 2000, "文件已提交，请勿重复提交");
        uploadFiles.forEach(file => {
          this.customUploadHandler({ file: file.raw }); // 确保传递的是单个文件对象
        });
        this.upload.open = false;
        this.$refs.upload.clearFiles();
        this.getList();
      } else {
        this.$message.error('请选择文件后再上传');
      }
    },
    // 导出数据
    handleExport() {
      this.handleExport = this.$throttle(this.handleExport, 2000, "请勿短时间频繁导出");
      const filename = `${new Date().getTime()}-员工数据.xlsx`;
      // 解构赋值，排除不需要的分页参数
      const { pageNum, pageSize, ...query } = this.queryParams;
      this.$download('/employee/export', query, filename)
        .then(() => {
        }).catch((error) => {
        });
      this.getList();
    },
    // 处理选中变化
    employeeSelectionChange(selection) {
      this.ids = selection.map(item => item.jobNumber)
      this.single = selection.length !== 1;
      this.multiple = selection.length === 0;

    },
    // 分页设置
    handleSizeChange(pageSize) {
      //改变时会触发	每页条数
      this.queryParams.pageSize = pageSize;
      this.getList();

    },
    handleCurrentChange(pageNum) {
      //改变时会触发	当前页
      this.queryParams.pageNum = pageNum;
      this.getList();
    },
    getGenderText(gender) {
      return gender === '0' ? '男' : '女';
    },
    // 修改员工状态
    handleActive(row) {
      if (this.user.positionId <= 3) {
        this.$model.msgError("您没有权限更改员工状态");
        row.active = row.active === 1 ? 0 : 1;
        return;
      }
      if (this.user.positionId <= row.positionId) {
        this.$model.msgError("您没有权限停用同等职位或更高职位的员工账户");
        row.active = row.active === 1 ? 0 : 1;
        return;
      }
      const action = row.active === 1 ? '停用' : '启用';
      const confirmMessage = `确定要${action} ${row.name} 的账户吗 ?`;
      this.$model.confirm(confirmMessage).then(() => {
        return request.put(`/employee/employeeStatus/${row.jobNumber}`);
      }).then((res) => {
        if (res.code == 200) {
          if (row.active === 1) this.$model.msgError(`${row.name} 的账户已停用`);
          if (row.active === 0) this.$model.msgSuccess(`${row.name} 的账户已启用`);
          this.getList();
        } else {
          row.active = row.active === 1 ? 0 : 1;
          this.$model.msgError(res.msg);
        }
      }).catch((error) => {
        row.active = row.active === 1 ? 0 : 1;
        if (error !== 'cancel') {
          this.$model.msgError(`${action}账户失败`);
        }
      });
    },
  },
  mounted() {
  },
};
</script>

<style scoped>
/* 去除按下时的样式变化 */
.el-button:active {
  background-color: unset !important;
  border-color: unset !important;
  color: unset !important;
  box-shadow: none !important;
}
</style>
