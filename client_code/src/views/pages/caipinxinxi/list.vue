<template>
  <div
    class="app-contain"
    :style="{
      padding: '0 10px',
      margin: '0 auto 20px',
      overflow: 'hidden',
      alignItems: 'flex-start',
      flexWrap: 'wrap',
      background: '#fff',
      display: 'flex',
      width: '1200px',
      position: 'relative',
    }"
  >
    <div class="back_view" v-if="centerType">
      <el-button class="back_btn" @click="backClick" type="primary"
        >返回</el-button
      >
    </div>
    <div class="bread_view">
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }"
          >首页</el-breadcrumb-item
        >
        <el-breadcrumb-item
          class="second_breadcrumb"
          v-for="(item, index) in breadList"
          :key="index"
          >{{ item.name }}</el-breadcrumb-item
        >
      </el-breadcrumb>
    </div>
    <el-form :inline="true" :model="searchQuery" class="list_search">
      <div class="search_view">
        <div class="search_label">菜品名称：</div>
        <div class="search_box">
          <el-input
            class="search_inp"
            v-model="searchQuery.caipinmingcheng"
            placeholder="菜品名称"
            clearable
          >
          </el-input>
        </div>
      </div>
      <div class="search_btn_view">
        <el-button class="search_btn" type="primary" @click="searchClick"
          >搜索</el-button
        >
        <el-button
          class="add_btn"
          type="success"
          v-if="btnAuth('caipinxinxi', '新增')"
          @click="addClick"
          >新增</el-button
        >
      </div>
    </el-form>
    <div class="category_view">
      <div
        class="category"
        :class="categoryIndex == -1 ? 'categoryActive' : ''"
        @click="categoryClick(-1)"
      >
        全部
      </div>
      <div
        class="category"
        :class="categoryIndex == index ? 'categoryActive' : ''"
        v-for="(item, index) in categoryList"
        :key="index"
        @click="categoryClick(index)"
      >
        {{ item }}
      </div>
    </div>
    <div class="list_bottom">
      <div class="data_box">
        <div class="data_view">
          <div
            class="data_item animation_box"
            v-for="(item, index) in list"
            :key="index"
            @click.stop="detailClick(item.id)"
          >
            <div
              class="data_img_box"
              v-if="
                item.caipintupian && item.caipintupian.substr(0, 4) == 'http'
              "
              @click.stop="preViewClick(item.caipintupian)"
            >
              <el-image
                class="data_img"
                :src="item.caipintupian"
                fit="cover"
              ></el-image>
            </div>
            <div
              class="data_img_box"
              v-else
              @click.stop="
                preViewClick($config.url + item.caipintupian.split(',')[0])
              "
            >
              <el-image
                class="data_img"
                :src="
                  item.caipintupian
                    ? $config.url + item.caipintupian.split(',')[0]
                    : ''
                "
                fit="cover"
              ></el-image>
            </div>
            <div class="data_content">
              <div class="data_title">{{ item.caipinmingcheng }}</div>
              <div class="data_price" v-if="item.price">￥{{ item.price }}</div>
            </div>
          </div>
        </div>
        <el-pagination
          background
          :layout="layouts.join(',')"
          :total="total"
          :page-size="listQuery.limit"
          prev-text="上一页"
          next-text="下一页"
          :hide-on-single-page="false"
          :style="{
            border: '1px solid #ddd',
            padding: '4px 0',
            margin: '10px 0 20px',
            whiteSpace: 'nowrap',
            color: '#333',
            textAlign: 'center',
            flexWrap: 'wrap',
            background:
              'linear-gradient(270deg, rgba(238,238,238,1) 0%, rgba(255,255,255,1) 50%, rgba(238,238,238,1) 100%)',
            display: 'flex',
            width: '100%',
            fontWeight: '500',
            justifyContent: 'center',
          }"
          @size-change="sizeChange"
          @current-change="currentChange"
          @prev-click="prevClick"
          @next-click="nextClick"
        />
      </div>
    </div>
    <el-dialog
      v-model="preViewVisible"
      :title="'查看大图'"
      width="60%"
      destroy-on-close
    >
      <img :src="preViewUrl" style="width: 100%" alt="" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const context = getCurrentInstance()?.appContext.config.globalProperties
const router = useRouter()
const route = useRoute()
//基础信息
const tableName = 'caipinxinxi'
const formName = '菜品信息'
//基础信息
const breadList = ref([
  {
    name: formName,
  },
])
const list = ref([])
const listQuery = ref({
  page: 1,
  limit: Number(8),
})
const total = ref(0)
const listLoading = ref(false)
//权限验证
const btnAuth = (e, a) => {
  if (centerType.value) {
    return context?.$toolUtil.isBackAuth(e, a)
  } else {
    return context?.$toolUtil.isAuth(e, a)
  }
}
const addClick = () => {
  router.push('/index/caipinxinxiAdd')
}
//判断是否从个人中心跳转
const centerType = ref(false)
//返回
const backClick = () => {
  router.push(
    `/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`
  )
}
const init = () => {
  if (route.query.centerType) {
    centerType.value = true
  }
  getCategoryList()
  getList()
}
//搜索
const searchQuery = ref({})
//下拉列表
const searchClick = () => {
  listQuery.value.page = 1
  getList()
}
//分页
const layouts = ref(['total', 'prev', 'pager', 'next', 'sizes', 'jumper'])
const sizeChange = (size) => {
  listQuery.value.limit = size
  getList()
}
const currentChange = (page) => {
  listQuery.value.page = page
  getList()
}
const prevClick = () => {
  listQuery.value.page = listQuery.value.page - 1
  getList()
}
const nextClick = () => {
  listQuery.value.page = listQuery.value.page + 1
  getList()
}
//分页
//列表
const getList = () => {
  listLoading.value = true
  let params = JSON.parse(JSON.stringify(listQuery.value))
  if (categoryIndex.value != -1) {
    params.caipinfenlei = categoryList.value[categoryIndex.value]
  }
  if (
    searchQuery.value.caipinmingcheng &&
    searchQuery.value.caipinmingcheng != ''
  ) {
    params.caipinmingcheng = '%' + searchQuery.value.caipinmingcheng + '%'
  }
  context
    ?.$http({
      url: `${tableName}/${centerType.value ? 'page' : 'list'}`,
      method: 'get',
      params: params,
    })
    .then((res) => {
      listLoading.value = false
      list.value = res.data.data.list
      total.value = Number(res.data.data.total)
    })
}
//分类
const categoryList = ref([])
const categoryIndex = ref(-1)
const getCategoryList = () => {
  context
    ?.$http({
      url: 'option/caipinfenlei/caipinfenlei',
      method: 'get',
    })
    .then((res) => {
      categoryList.value = res.data.data
    })
}
const categoryClick = (index) => {
  listQuery.value.page = 1
  categoryIndex.value = index
  getList()
}
const detailClick = (id) => {
  router.push(
    'caipinxinxiDetail?id=' + id + (centerType.value ? '&&centerType=1' : '')
  )
}
//下载文件
const download = (file) => {
  if (!file) {
    context?.$toolUtil.message('文件不存在', 'error')
  }
  const a = document.createElement('a')
  a.style.display = 'none'
  a.setAttribute('target', '_blank')
  file && a.setAttribute('download', file)
  a.href = context?.$config.url + file
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
}
// 查看大图
const preViewUrl = ref('')
const preViewVisible = ref(false)
const preViewClick = (url) => {
  preViewUrl.value = url
  preViewVisible.value = true
}
init()
</script>
<style lang="scss" scoped>
// 返回盒子
.back_view {
  border-radius: 0px;
  padding: 0 20px;
  margin: 0;
  background: none;
  display: inline-block;
  width: 100%;
  text-align: right;
  // 返回按钮
  .back_btn {
    border: 1px solid #eee;
    cursor: pointer;
    border-radius: 4px;
    padding: 0 20px;
    color: #666;
    background: #fff;
    width: auto;
    font-size: 14px;
    height: 24px;
  }
  // 返回按钮-悬浮
  .back_btn:hover {
  }
}
.bread_view {
  border-radius: 0px;
  padding: 0px 20px 10px;
  margin: 0px auto;
  background: none;
  width: calc(100% - 0px);
  border-color: #66666650;
  border-width: 0 0 1px;
  position: relative;
  border-style: solid;
  :deep(.breadcrumb) {
    font-size: 14px;
    line-height: 1;
    .el-breadcrumb__separator {
      margin: 0 9px;
      color: #333;
      font-weight: 500;
    }
    .first_breadcrumb {
      .el-breadcrumb__inner {
        color: #333;
        display: inline-block;
      }
    }
    .second_breadcrumb {
      .el-breadcrumb__inner {
        color: #333;
        display: inline-block;
      }
    }
  }
}
// 分类盒子
.category_view {
  border: 1px solid #cfe4ba;
  padding: 20px 10px 0;
  margin: 20px 30px 0 0;
  display: flex;
  border-color: #ddd;
  flex-wrap: wrap;
  border-radius: 8px 8px 0 0;
  background: none;
  width: 220px;
  justify-content: center;
  border-width: 1px;
  border-style: solid;
  order: 1;
  // 分类item
  .category {
    cursor: pointer;
    padding: 0;
    margin: 0px auto 10px;
    color: #333;
    font-size: 14px;
    border-color: #fff;
    line-height: 48px;
    box-sizing: border-box;
    border-radius: 8px 8px 0 0;
    background: none;
    width: 100%;
    border-width: 0px;
    border-style: solid;
    text-align: center;
    height: 48px;
  }
  // item-悬浮
  .category:hover {
    color: #fff;
    background: url(http://localhost:8080/onlineOrder/file/d7ebcef5ff574020b1b67cffe59c5a88.jpg)
      no-repeat center top / 100% 100%;
    border-color: #27bacc;
    border-width: 0px;
    opacity: 1;
    border-style: solid;
  }
  // item-选中
  .categoryActive {
    border-radius: 8px 8px 0 0;
    color: #fff;
    background: url(http://localhost:8080/onlineOrder/file/d7ebcef5ff574020b1b67cffe59c5a88.jpg)
      no-repeat center top / 100% 100%;
    font-size: 14px;
    border-color: #27bacc;
    border-width: 0px;
    opacity: 1;
    border-style: solid;
  }
}

//搜索
.list_search {
  border: 1px solid #ddd;
  padding: 0;
  margin: 0;
  background: none;
  display: flex;
  width: calc(100% - 0px);
  justify-content: center;
  align-items: center;
  .search_view {
    margin: 0 10px 0 0;
    display: flex;
    align-items: center;
    .search_label {
      margin: 0 10px 0 0;
      color: #333;
      font-weight: 500;
      display: inline-block;
      font-size: 14px;
      line-height: 40px;
      height: 40px;
    }
    .search_box {
      display: flex;
      width: calc(100% - 100px);
      // 输入框
      :deep(.search_inp) {
        border-radius: 20px;
        padding: 0 10px;
        background: none;
        width: 100%;
        border-color: #ddd;
        border-width: 1px;
        line-height: 40px;
        box-sizing: border-box;
        border-style: solid;
        min-width: 200px;
        height: 40px;
        //去掉默认样式
        .el-input__wrapper {
          border: none;
          box-shadow: none;
          background: none;
          border-radius: 0;
          height: 100%;
          padding: 0;
        }
        .is-focus {
          box-shadow: none !important;
        }
      }
    }
  }
  .search_btn_view {
    margin: 20px 0;
    display: flex;
    // 搜索按钮
    .search_btn {
      border: 0;
      cursor: pointer;
      border-radius: 20px;
      padding: 0 24px;
      margin: 0 10px 0 0;
      color: #fff;
      background: #d65e29;
      width: auto;
      font-size: 14px;
      height: 40px;
    }
    // 搜索按钮-悬浮
    .search_btn:hover {
      opacity: 0.8;
    }
    // 新增按钮
    .add_btn {
      border: 0;
      cursor: pointer;
      border-radius: 20px;
      padding: 0 24px;
      margin: 0 10px 0 0;
      color: #fff;
      background: #666;
      width: auto;
      font-size: 14px;
      height: 40px;
    }
    // 新增按钮-悬浮
    .add_btn:hover {
      opacity: 0.8;
    }
  }
}

// 数据盒子
.list_bottom {
  padding: 0 0 20px;
  background: none;
  flex: 1;
  display: flex;
  width: calc(100% - 0px);
  align-items: flex-start;
  flex-wrap: wrap;
  order: 2;
  //列表
  .data_box {
    border: 0px solid #ddd;
    padding: 0;
    margin: 20px 0 0;
    background: none;
    width: calc(100% - 0px);
    order: 10;
    .data_view {
      border: 0px solid #eee;
      padding: 0px;
      overflow: hidden;
      background: none;
      display: flex;
      width: 100%;
      flex-wrap: wrap;
      .data_item {
        cursor: pointer;
        border: 0px solid #ddd;
        padding: 0px;
        margin: 0 2% 24px 0;
        background: none;
        width: calc(31% - 0px);
        box-sizing: border-box;
        // 图片盒子
        .data_img_box {
          margin: 0 auto 2px;
          width: 100%;
          position: relative;
          height: 180px;
          // 图片
          .data_img {
            border: 0px solid #eee;
            border-radius: 20px;
            padding: 0px;
            object-fit: cover;
            width: 100%;
            height: 100%;
          }
        }
        // 内容盒子
        .data_content {
          display: flex;
          width: 100%;
          justify-content: flex-start;
          flex-wrap: wrap;
          // 价格
          .data_price {
            padding: 0 0px 0 0;
            color: #c00;
            width: 100%;
            font-size: 16px;
            text-align: center;
            order: 2;
          }
          // 标题
          .data_title {
            padding: 0 0px;
            margin: 0 0 0px;
            overflow: hidden;
            color: #000;
            white-space: nowrap;
            background: none;
            width: 100%;
            font-size: 14px;
            line-height: 30px;
            text-overflow: ellipsis;
            text-align: center;
            height: 30px;
          }
          // 底部栏
          .data_operate_box {
            padding: 6px 0;
            color: #999;
            display: flex;
            width: 100%;
            justify-content: center;
            align-items: center;
            box-sizing: border-box;
            order: 3;
            // 点赞数
            .data_like {
              margin: 0 10px 0 0;
              color: inherit;
              display: flex;
              font-size: 14px;
              align-items: center;
              .like_icon {
                margin: 0 4px 0 0;
                color: inherit;
              }
              .like_num {
                color: inherit;
              }
            }
            // 收藏数
            .data_collect {
              margin: 0 10px 0 0;
              color: inherit;
              display: flex;
              font-size: 14px;
              align-items: center;
              .el-icon {
                margin: 0 4px 0 0;
                color: inherit;
              }
              .collect_num {
                color: inherit;
              }
            }
            // 点击数
            .data_clickNum {
              margin: 0 10px 0 0;
              color: inherit;
              display: flex;
              font-size: 14px;
              align-items: center;
              .el-icon {
                margin: 0 4px 0 0;
                color: inherit;
              }
              .clickNum_num {
                color: inherit;
              }
            }
          }
        }
      }
    }
  }
}

// animation
.animation_box {
  transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
  z-index: initial;
}
.animation_box:hover {
  transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
  -webkit-perspective: 1000px;
  perspective: 1000px;
  transition: 0.3s;
}
.animation_box img {
  transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
  z-index: initial;
}
.animation_box img:hover {
  transform: rotate(0deg) scale(1.05) skew(0deg, 0deg)
    translate3d(0px, 0px, 0px);
  -webkit-perspective: 1000px;
  perspective: 1000px;
  transition: 0.3s;
}
// animation

// 分页器
.el-pagination {
  // 总页码
  :deep(.el-pagination__total) {
    margin: 0 10px 0 0;
    color: #666;
    font-weight: 400;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 24px;
    height: 24px;
  }
  // 上一页
  :deep(.btn-prev) {
    border: 0px solid #ddd;
    border-radius: 4px;
    padding: 0 4px;
    margin: 0 2px;
    color: #666;
    background: none;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 24px;
    min-width: 24px;
    height: 24px;
  }
  // 下一页
  :deep(.btn-next) {
    border: 0px solid #ddd;
    border-radius: 4px;
    padding: 0 4px;
    margin: 0 2px;
    color: #666;
    background: none;
    display: inline-block;
    vertical-align: top;
    font-size: 14px;
    line-height: 24px;
    min-width: 24px;
    height: 24px;
  }
  // 上一页禁用
  :deep(.btn-prev:disabled) {
    border: 0px solid #ddd;
    cursor: not-allowed;
    padding: 0 4px;
    margin: 0 2px;
    color: #c0c4cc;
    display: inline-block;
    vertical-align: top;
    font-size: 14px;
    line-height: 24px;
    border-radius: 4px;
    background: none;
    min-width: 24px;
    height: 24px;
  }
  // 下一页禁用
  :deep(.btn-next:disabled) {
    border: 0px solid #ddd;
    cursor: not-allowed;
    padding: 0 4px;
    margin: 0 2px;
    color: #c0c4cc;
    display: inline-block;
    vertical-align: top;
    font-size: 14px;
    line-height: 24px;
    border-radius: 4px;
    background: none;
    min-width: 24px;
    height: 24px;
  }
  // 页码
  :deep(.el-pager) {
    padding: 0;
    margin: 0;
    display: inline-block;
    vertical-align: top;
    // 数字
    .number {
      cursor: pointer;
      padding: 0 4px;
      margin: 0 5px;
      color: #666;
      display: inline-block;
      vertical-align: top;
      font-size: 13px;
      line-height: 24px;
      border-radius: 0;
      background: #fff;
      text-align: center;
      min-width: 24px;
      height: 24px;
    }
    // 数字悬浮
    .number:hover {
      cursor: pointer;
      padding: 0 4px;
      margin: 0 5px;
      color: #fff;
      display: inline-block;
      vertical-align: top;
      font-size: 13px;
      line-height: 24px;
      border-radius: 50px;
      background: #d65e29;
      text-align: center;
      min-width: 24px;
      height: 24px;
    }
    // 选中
    .number.is-active {
      cursor: default;
      padding: 0 4px;
      margin: 0 5px;
      color: #fff;
      display: inline-block;
      vertical-align: top;
      font-size: 13px;
      line-height: 24px;
      border-radius: 50px;
      background: #d65e29;
      text-align: center;
      min-width: 24px;
      height: 24px;
    }
  }
  // sizes
  :deep(.el-pagination__sizes) {
    box-shadow: none;
    margin: 0 0 0 5px;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 24px;
    height: 24px;
    .el-select {
      border: 0px solid #dcdfe6;
      cursor: pointer;
      border-radius: 2px;
      padding: 0;
      color: #606266;
      background: #fff;
      display: inline-block;
      width: 100%;
      font-size: 13px;
      line-height: 24px;
      text-align: center;
      height: 24px;
      //去掉默认样式
      .select-trigger {
        height: 100%;
        .el-input {
          height: 100%;
          .el-input__wrapper {
            border: none;
            box-shadow: none;
            background: none;
            border-radius: 0;
            height: 100%;
            padding: 0;
          }
          .is-focus {
            box-shadow: none !important;
          }
        }
      }
    }
  }
  // 跳页
  :deep(.el-pagination__jump) {
    margin: 0 0 0 24px;
    color: #606266;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
    // 输入框
    .el-input {
      border: 1px solid #eee;
      cursor: pointer;
      padding: 0 3px;
      margin: 0 6px;
      color: #606266;
      display: inline-block;
      font-size: 14px;
      line-height: 24px;
      border-radius: 3px;
      background: #fff;
      width: 38px;
      text-align: center;
      height: 24px;
      //去掉默认样式
      .el-input__wrapper {
        border: none;
        box-shadow: none;
        background: none;
        border-radius: 0;
        height: 100%;
        padding: 0;
      }
      .is-focus {
        box-shadow: none !important;
      }
    }
  }
}

// 热门信息盒子
.hot_view {
  border: 0px solid #ddd;
  padding: 0;
  margin: 0px 20px 0 0;
  background: none;
  width: 100%;
  order: 20;
  // 标题
  .hot_title {
    padding: 0 20px;
    margin: 20px auto;
    color: #fff;
    background: url(http://localhost:8080/onlineOrder/file/0bbe816716c34dfd909ce68a72979f14.jpg)
      no-repeat center top / 100% 100%;
    font-weight: 500;
    width: 100%;
    font-size: 18px;
    border-color: #66666650;
    border-width: 0 0 0px;
    line-height: 48px;
    border-style: solid;
    text-align: left;
  }

  .hot_list {
    padding: 0 0 0 2%;
    margin: 30px 0 0;
    display: flex;
    width: 100%;
    flex-wrap: wrap;
    // item
    .hot {
      cursor: pointer;
      border: 0px solid #eee;
      padding: 0px;
      margin: 0 2% 20px 0;
      background: none;
      width: 31%;
      box-sizing: border-box;
      //图片盒子
      .hot_img_view {
        margin: 0 0 2px;
        width: 100%;
        font-size: 0;
        height: 150px;
        // 图片
        .hot_img {
          border-radius: 20px;
          object-fit: cover;
          width: 100%;
          height: 100%;
        }
      }
      // 内容盒子
      .hot_content {
        // 名称
        .hot_text {
          padding: 0 16px;
          margin: 0 0 0px;
          overflow: hidden;
          color: #333;
          white-space: nowrap;
          background: none;
          font-size: 14px;
          line-height: 30px;
          text-overflow: ellipsis;
          text-align: center;
          height: 30px;
        }
      }
    }
  }
}
</style>
