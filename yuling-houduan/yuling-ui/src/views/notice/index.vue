<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="公告标题" prop="noticeTitle">
                <el-input v-model="queryParams.noticeTitle" placeholder="请输入公告标题" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="发布单位" prop="unitDepartment">
                <el-input v-model="queryParams.unitDepartment" placeholder="请选择发布单位" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="公告类型" prop="noticeType">
                <el-select v-model="queryParams.noticeType" placeholder="请选择公告类型" clearable>
                    <el-option :key="0" :label="'通知'" :value="'通知'" />
                    <el-option :key="1" :label="'公告'" :value="'公告'" />
                </el-select>
            </el-form-item>
            <el-form-item label="公告状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="请选择公告状态" clearable>
                    <el-option :key="0" :label="'正常'" :value="0" />
                    <el-option :key="1" :label="'停用'" :value="1" />
                </el-select>

            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
                    @click="handleUpdate">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="noticeList" width="100%" @selection-change="handleSelectionChange"
            :row-style="{ height: '45px' }" :cell-style="{ padding: '0' }">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="序号" align="center" prop="id" />
            <el-table-column label="公告标题" align="center" prop="noticeTitle" :show-overflow-tooltip="true" />
            <el-table-column label="公告类型" align="center" prop="noticeType">
            </el-table-column>
            <el-table-column label="状态" align="center" prop="status">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" :active-value="'0'" :inactive-value="'1'"
                        active-color="#71c9ce" @change="handleStatus(scope.row)">
                    </el-switch>
                </template>
            </el-table-column>
            <el-table-column label="创建者" align="center" prop="unitDepartment" />
            <el-table-column label="创建时间" align="center" prop="createTime" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" icon="el-icon-edit"
                        @click="handleUpdate(scope.row)">修改</el-button>
                    <el-button size="mini" type="danger" icon="el-icon-delete"
                        @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div style="margin-top: 1px; width: auto;text-align: center;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="queryParams.pageNum" :page-sizes="[10, 16, 30, 40]" :page-size="queryParams.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="total">
            </el-pagination>
        </div>
        <!-- 添加或修改公告对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="公告标题" prop="noticeTitle">
                            <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="公告类型" prop="noticeType">
                            <el-select v-model="form.noticeType" placeholder="请选择公告类型">
                                <el-option :key="0" :label="'通知'" :value="'通知'" />
                                <el-option :key="1" :label="'公告'" :value="'公告'" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="状态" prop="status">
                            <el-radio-group v-model="form.status">
                                <el-radio :label="'0'">正常</el-radio>
                                <el-radio :label="'1'">停用</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="内容">
                            <editor v-model="form.noticeContent" :min-height="192" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from '@/utils/request';
import quill from '@/components/quill.vue';
export default {
    name: "Notice",
    components:{
        editor:quill
    },
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 公告表格数据
            noticeList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                noticeTitle: null,
                noticeType: null,
                noticeContent: null,
                utilDepartment: null,
            },
            // 表单参数
            form: {
                status: 0,
                noticeContent:'',
            },
            // 表单校验
            rules: {
                noticeTitle: [
                    { required: true, message: "公告标题不能为空", trigger: "blur" }
                ],
                noticeType: [
                    { required: true, message: "公告类型不能为空", trigger: "change" }
                ]
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询公告列表 */
        getList() {
            this.loading = true;
            request.get("/notice/getNotice", { params: this.queryParams }).then(res => {
                if (res.code == 200) {
                    console.log(res.data.list);

                    this.noticeList = res.data.list;
                    this.total = res.data.total;
                    this.loading = false;
                }
            });
            this.loading = false;
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                id: '',
                noticeTitle: '',
                noticeType: '',
                noticeContent: '',
                status: '0'
            };
            this.queryParams = {
                pageNum: 1,
                pageSize: 10,
                noticeTitle: null,
                noticeType: null,
                noticeContent: null,
                utilDepartment: null,
                status: null
            };
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.reset();
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.id)
            this.single = selection.length != 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加公告";
        },
        handleStatus(row) {
            const action = row.status === '1' ? '关闭' : '开启';
            const confirmMessage = `确定要${action} ${row.noticeTitle} 公告吗 ?`;
            this.$model.confirm(confirmMessage).then(() => {
                return request.put(`/notice/updateStatus/${row.id}`);
            }).then((res) => {
                if (res.code == 200) {
                    if (row.status === '1') this.$model.msgError(`${row.noticeTitle} 公告已关闭`);
                    if (row.status === '0') this.$model.msgSuccess(`${row.noticeTitle} 公告已开启`);
                    this.getList();
                } else {
                    row.status = row.status === '1' ? '0' : '1';
                    this.$model.msgError(res.msg);
                }
            }).catch((error) => {
                row.status = row.status === '1' ? '0' : '1';
                if (error !== 'cancel') {
                }
            });
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            request.get(`/notice/getNoticeById?id=${id}`).then(res => {
                if (res.code == 200) {
                    this.form = res.data;
                    this.open = true;
                    this.title = "修改公告";
                } else {
                    if (res.msg !== "token验证失败，请重新登录") {
                        this.$message.error("系统故障，请稍后再试");
                    }
                }
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != '') {
                        request.put('/notice/updateNotice', this.form).then(res => {
                            console.log(this.form);
                            
                            if (res.code == 200) {
                                this.$model.msgSuccess("修改成功");
                                this.open = false;
                                this.getList();
                            }
                        });
                    } else {
                        request.post('/notice/insertNotice', this.form).then(res => {
                            this.$model.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.id || this.ids
            this.$model.confirm('是否确认删除公告编号为"' + ids + '"的数据项？').then(function () {
                return request.delete(`/notice/deleteNoticeByIds/${ids}`);
            }).then(() => {
                this.getList();
                this.$model.msgSuccess("删除成功");
            }).catch(() => { });
        },
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
    }
};
</script>