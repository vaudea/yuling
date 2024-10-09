<template>
    <div id="app-container">

        <!-- 搜索表单 -->
        <div class="form-container" style="margin-left: -10px;margin-top: -8px ;"> <!-- 左移表单 -->
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
                <el-form-item label="支付日期" prop="payDate">
                    <el-date-picker v-model="queryParams.queryPayDate" type="month" value-format="yyyy-MM"
                        placeholder="请选择支付日期" clearable @keyup.enter.native="getList" style="width: 100%;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="支付方式" prop="paymentMethod">
                    <el-select v-model="queryParams.paymentMethod" placeholder="支付方式">
                        <el-option label="银行卡" value="银行卡" />
                        <el-option label="现金" value="现金" />
                        <el-option label="微信" value="微信" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" size="mini" @click="findBySearch">搜索</el-button>
                    <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 个人薪资表格 -->
        <el-table v-loading="loading" :data="list" width="100%" :row-style="{ height: '45px' }"
            :cell-style="{ padding: '0' }">
            <el-table-column label="员工工号" align="center" prop="jobNumber" width="106" />
            <el-table-column label="支付日期" align="center" prop="payDate" width="95" />
            <el-table-column label="基本工资(￥)" align="center" prop="basicSalary" />
            <el-table-column label="加班费(￥)" align="center" prop="overtimePay" width="106" />
            <el-table-column label="奖金(￥)" align="center" prop="bonus" />
            <el-table-column label="提成(￥)" align="center" prop="commission" />
            <el-table-column label="津贴(￥)" align="center" prop="allowance" />
            <el-table-column label="五险一金(￥)" align="center" prop="fiveInsurancesOneFund" />
            <el-table-column label="净工资(￥)" align="center" prop="netSalary" />
            <el-table-column label="银行卡号" align="center" prop="bankCard" width="145" />
            <el-table-column label="支付方式" align="center" prop="paymentMethod" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
                <template slot-scope="scope">
                    <el-button size="mini" type="primary" icon="el-icon-edit"
                        @click="particularsSalay(scope.row)">详情</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div style="margin-top: 5px;width: auto;text-align: center;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="queryParams.pageNum" :page-sizes="[5, 10, 15, 20]" :page-size="queryParams.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="total">
            </el-pagination>
        </div>


        <!-- 薪资详情查看 -->
        <el-dialog title="薪资详情" :visible.sync="open" width="600px" append-to-body>
            <el-form ref="form" :model="form" label-width="110px">
                <el-form-item label="员工工号" prop="jobNumber">
                    <el-input v-model="form.jobNumber" />
                </el-form-item>
                <el-form-item label="支付日期" prop="payDate">
                    <el-input v-model="form.payDate" />
                </el-form-item>
                <el-form-item label="基本工资(￥)" prop="basicSalary">
                    <el-input v-model="form.basicSalary" />
                </el-form-item>
                <el-form-item label="加班费(￥)" prop="overtimePay">
                    <el-input v-model="form.overtimePay" />
                </el-form-item>
                <el-form-item label="奖金(￥)" prop="bonus">
                    <el-input v-model="form.bonus" />
                </el-form-item>
                <el-form-item label="提成(￥)" prop="commission">
                    <el-input v-model="form.commission" />
                </el-form-item>
                <el-form-item label="津贴(￥)" prop="allowance">
                    <el-input v-model="form.allowance" />
                </el-form-item>
                <el-form-item label="扣款(￥)" prop="deductions">
                    <el-input v-model="form.deductions" />
                </el-form-item>
                <el-form-item label="支付方式" prop="paymentMethod">
                    <el-input v-model="form.paymentMethod" />
                </el-form-item>
                <el-form-item label="扣款备注(￥)" prop="remarks ">
                    <el-input v-model="form.remarks" placeholder="请输入扣款备注" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: center;margin-top: -50px;">
                <el-button @click="close" type="primary" style="text-align: center;">关 闭</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
import request from '@/utils/request';
export default {
    data() {
        return {
            loading: false,
            list: [],
            total: 0,
            queryParams: {
                pageNum: 1, // 当前页码
                pageSize: 5, // 每页显示条数
                jobNumber: null,
                queryPayDate: null,
                paymentMethod: null
            },
            open: false,
            form: {},
        }
    },
    created() {
        this.reset();
        this.getList();
    },
    methods: {
        getList() {
            this.queryParams.jobNumber = JSON.parse(this.$cookies.get('user')).jobNumber;
            request.get('/salary/getSalary', { params: this.queryParams }).then(res => {
                if (res.code == 200) {
                    // console.log(res.data.list);
                    this.list = res.data.list;
                    this.total = res.data.total;
                    this.loading = false;
                } else {
                    this.$message.error(res.msg);
                }
            })
            this.loading = false;
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
                //查询属性名
                jobNumbe: null,
                queryPayDate: null,
                paymentMethod: null,

            };
            this.getList();
        },
        particularsSalay(row) {
            this.reset();
            this.form = Object.assign({}, row); // 使用对象复制防止直接修改绑定数据
            this.open = true;
        },
        reset() {
            this.form = {};
            this.queryParams = {
                pageNum: 1, // 当前页码
                pageSize: 5, // 每页显示条数
                jobNumber: null,
                queryPayDate: null,
                paymentMethod: null
            }
        },
        close() {
            this.reset();
            this.open = false;
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
    }
}
</script>

<style></style>