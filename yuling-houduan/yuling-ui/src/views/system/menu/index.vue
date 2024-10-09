<template>
    <div class="app-container">

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="info" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">展开/折叠</el-button>
            </el-col>
        </el-row>

        <el-table v-if="refreshTable" v-loading="loading" :data="menuList" row-key="id"
            :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            :row-style="{ height: '48.8px' }" :cell-style="{ padding: '0' }">
            <el-table-column prop="title" label="菜单名称" :show-overflow-tooltip="true" width="160" />
            <el-table-column prop="icon" label="图标" align="center" width="100">
                <template slot-scope="scope">
                    <i :class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
            <el-table-column prop="router" label="路由地址" :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="success" icon="el-icon-edit"
                        @click="handleUpdate(scope.row)">修改</el-button>
                    <el-button size="mini" type="info" icon="el-icon-plus" @click="handleAdd(scope.row)">新增</el-button>
                    <el-button size="mini" type="danger" icon="el-icon-delete"
                        @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>


        <!-- 添加或修改菜单对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="680px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="上级菜单" prop="parentId">
                            <treeselect v-model="form.parentId" :options="menuOptions" :normalizer="normalizer"
                                :show-count="true" placeholder="选择上级菜单" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="菜单图标" prop="icon">
                            <el-popover ref="popoverRef" placement="bottom-start" width="460" trigger="click"
                                @show="handlePopoverShow" :visible.sync="popoverVisible" :style="{ zIndex: 2000 }">
                                <icon-select ref="iconSelect" @selected="selectedIcon" />
                                <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                                    <i :class="form.icon" slot="prefix" v-if="form.icon" style="width: 25px;"></i>
                                    <i v-else slot="prefix" class="el-icon-search el-input__icon"></i>
                                </el-input>
                            </el-popover>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="菜单名称" prop="title">
                            <el-input v-model="form.title" placeholder="请输入菜单名称" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="router">
                            <span slot="label">
                                <el-tooltip content="路由与组件地址一致 如：`/system/menu` 为路由  组件路径为@/view/system/menu/index"
                                    placement="top">
                                    <i class="el-icon-question"></i>
                                </el-tooltip>
                                路由地址
                            </span>
                            <el-input v-model="form.router" placeholder="请输入路由地址" />
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
import IconSelect from '@/components/IconSelect.vue';
import request from '@/utils/request';
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { resetDynamicRoutes } from '@/router/index';
export default {
    name: "Menu",
    components: {
        Treeselect,
        IconSelect
    },
    data() {
        return {
            popoverVisible: false,  // 控制Popover的显示状态
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: true,
            // 菜单表格树数据
            menuList: [],
            // 菜单树选项
            menuOptions: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 是否展开，默认全部折叠
            isExpandAll: false,
            // 重新渲染表格状态
            refreshTable: true,
            // 表单参数
            form: {
                id: undefined,
                icon: undefined,
                parentId: undefined,
                title: undefined,
                router: undefined,
            },
            // 表单校验
            rules: {
                title: [
                    { required: true, message: "菜单名称不能为空", trigger: "blur" }
                ],
                router: [
                    { required: true, message: "路由地址不能为空", trigger: "blur" }
                ]
            }
        };
    },
    created() {
        this.getList();

    },
    methods: {
        handlePopoverShow() {
            this.$refs.iconSelect.reset();
        },        // 选择图标
        selectedIcon(icon) {
            this.form.icon = icon;
            this.$nextTick(() => {
                if (this.$refs.popoverRef) {
                    this.$refs.popoverRef.doClose();
                }
            });
        },
        /** 查询菜单列表 */
        getList() {
            this.loading = true;
            request.get("/menu/getMenus").then((res) => {
                if (res.code == 200) {
                    // console.log(res);
                    this.menuList = res.data.body;
                    this.updateRouter(); // 
                    this.loading = false;
                }
            });
            this.loading = false;

        },
        updateRouter() {
            const updateComponent = (item) => {
                if (!item.parentId) { // 检查 parentId 是否为空
                    item.router = '';
                    item.component = '';
                } else {
                    item.component = item.router + '/index';
                }
                if (item.children && item.children.length > 0) {
                    item.children.forEach(child => updateComponent(child));
                }
            };
            this.menuList.forEach(item => updateComponent(item));
        },
        /** 转换菜单数据结构 */
        normalizer(node) {
            if (node.children && !node.children.length) {
                delete node.children;
            }
            return {
                id: node.id,
                label: node.title,
                children: node.children
            };
        },
        /** 查询菜单下拉树结构 */
        getTreeselect() {
            request.get("/menu/getMenus").then((res) => {
                if (res.code == 200) {
                    this.menuOptions = [];
                    const menu = { id: 0, title: '主类目', children: [] };
                    menu.children = this.handleTree(res.data.body);
                    this.menuOptions.push(menu);
                } else {
                    return
                }
            });
        },
        handleTree(data) {
            // 创建一个映射来存储每个节点的引用
            let idToNodeMap = {};
            // 创建一个数组用于存储根节点
            let tree = [];
            // 遍历数据并创建节点映射
            data.forEach(item => {
                // 使用浅拷贝确保不修改原始数据，并初始化 children 数组
                idToNodeMap[item.id] = { ...item, children: item.children ? [...item.children] : [] };
            });
            // 构建树结构
            data.forEach(item => {
                if (item.parentId && idToNodeMap[item.parentId]) {
                    // 如果有父节点且父节点存在，则将当前节点添加到父节点的 children 数组
                    idToNodeMap[item.parentId].children.push(idToNodeMap[item.id]);
                } else {
                    // 没有父节点的节点是根节点，直接添加到 tree 数组中
                    tree.push(idToNodeMap[item.id]);
                }
            });

            return tree;
        }
        ,
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                id: undefined,
                icon: undefined,
                parentId: undefined,
                title: undefined,
                router: undefined,
            };
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.handleQuery();
        },
        /** 新增按钮操作 */
        handleAdd(row) {
            this.reset();
            this.getTreeselect();
            if (row != null && row.id) {
                this.form.parentId = row.id;
            } else {
                this.form.parentId = 0;
            }
            this.open = true;
            this.title = "添加菜单";
        },
        /** 展开/折叠操作 */
        toggleExpandAll() {
            this.refreshTable = false;
            this.isExpandAll = !this.isExpandAll;
            this.$nextTick(() => {
                this.refreshTable = true;
            });
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            this.getTreeselect();
            request.get(`/menu/getMenuById?id=${row.id}`).then(res => {
                if (res.code == 200) {
                    this.form = res.data;
                    if (this.form.parentId === null) {
                        this.form.parentId = 0;
                    }
                    this.open = true;
                    this.title = "修改菜单";
                } else {
                    return
                }
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != undefined) {
                        this.submitForm = this.$throttle(this.submitForm, 2000, "数据已经提交请勿重复提交");
                        request.put("/menu/updateMenu", this.form).then(res => {
                            if (res.code == 200) {
                                resetDynamicRoutes();
                                this.$model.msgSuccess("修改成功");
                                window.location.reload();
                                // 手动触发更新 openTab 的逻辑
                                const routeTitle = this.form.title;
                                const routeIndex = this.form.router;
                                console.log(routeTitle+"  :  "+ routeIndex);
                                
                                if (routeTitle && !this.$store.state.openTab.some(tab => tab.index === routeIndex)) {
                                    this.$store.dispatch('modifyIndexByTitle', { title: routeTitle, newIndex: routeIndex });
                                }
                                if (routeIndex && !this.$store.state.openTab.some(tab => tab.title === routeTitle)) {
                                    this.$store.dispatch('modifyTitleByIndex', { index: routeIndex, newTitle: routeTitle });
                                }
                                // this.$store.dispatch('removeInvalidTabs');

                                this.open = false;
                                window.location.reload();
                            } else {
                                this.$model.msgError(res.msg);
                                this.open = false;
                            }
                        });
                    } else {
                        this.submitForm = this.$throttle(this.submitForm, 2000, "数据已经提交请勿重复提交");
                        request.post("/menu/insertMenu", this.form).then(res => {
                            if (res.code == 200) {
                                resetDynamicRoutes();
                                this.$model.msgSuccess("新增成功");
                                this.open = false;
                                window.location.reload();
                            } else {
                                this.$model.msgError(res.msg);
                                this.open = false;
                            }
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            this.$model.confirm('是否确认删除名称为"' + row.title + '"的菜单？').then(function () {
                return request.delete(`/menu/deleteMenuById/${row.id}`);
            }).then((res) => {
                if (res.code == 200) {
                    window.location.reload();
                    this.$model.msgSuccess("删除成功");
                } else {
                    window.location.reload();
                    this.$model.msgSuccess("删除失败: " + res.msg);
                }
            }).catch(() => { });
        }
    }
};
</script>

<style scoped></style>