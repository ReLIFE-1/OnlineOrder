<template>
  <div
    class="app-contain"
    :style="{
      minHeight: '100vh',
      padding: '10px 10px',
      margin: '0 auto 20px',
      borderRadius: '0',
      background: '#fff',
      width: '1200px',
      position: 'relative',
      height: '100%',
    }"
  >
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
    <el-form
      ref="formRef"
      :model="form"
      class="add_form"
      label-width="120px"
      :rules="rules"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="店铺名称" prop="dianpumingcheng">
            <el-input
              class="list_inp"
              v-model="form.dianpumingcheng"
              placeholder="店铺名称"
              type="text"
              :readonly="!isAdd || disabledForm.dianpumingcheng ? true : false"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="菜品名称" prop="caipinmingcheng">
            <el-input
              class="list_inp"
              v-model="form.caipinmingcheng"
              placeholder="菜品名称"
              type="text"
              :readonly="!isAdd || disabledForm.caipinmingcheng ? true : false"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="菜品图片" prop="caipintupian">
            <uploads
              :disabled="!isAdd || disabledForm.caipintupian ? true : false"
              action="file/upload"
              tip="请上传菜品图片"
              :limit="3"
              style="width: 100%; text-align: left"
              :fileUrls="form.caipintupian ? form.caipintupian : ''"
              @change="caipintupianUploadSuccess"
            >
            </uploads>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="菜品分类" prop="caipinfenlei">
            <el-select
              class="list_sel"
              :disabled="!isAdd || disabledForm.caipinfenlei ? true : false"
              v-model="form.caipinfenlei"
              placeholder="请选择菜品分类"
              style="width: 100%"
            >
              <el-option
                v-for="(item, index) in caipinfenleiLists"
                :label="item"
                :value="item"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="口味" prop="kouwei">
            <el-select
              class="list_sel"
              :disabled="!isAdd || disabledForm.kouwei ? true : false"
              v-model="form.kouwei"
              placeholder="请选择口味"
              style="width: 100%"
            >
              <el-option
                v-for="(item, index) in kouweiLists"
                :label="item"
                :value="item"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="推荐指数" prop="tuijianzhishu">
            <el-select
              class="list_sel"
              :disabled="!isAdd || disabledForm.tuijianzhishu ? true : false"
              v-model="form.tuijianzhishu"
              placeholder="请选择推荐指数"
              style="width: 100%"
            >
              <el-option
                v-for="(item, index) in tuijianzhishuLists"
                :label="item"
                :value="item"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="营养成分" prop="yingyangchengfen">
            <el-input
              class="list_inp"
              v-model="form.yingyangchengfen"
              placeholder="营养成分"
              type="text"
              :readonly="!isAdd || disabledForm.yingyangchengfen ? true : false"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="价格" prop="price">
            <el-input
              class="list_inp"
              v-model.number="form.price"
              placeholder="价格"
              type="number"
              :readonly="!isAdd || disabledForm.price ? true : false"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="商家账号" prop="shangjiazhanghao">
            <el-input
              class="list_inp"
              v-model="form.shangjiazhanghao"
              placeholder="商家账号"
              type="text"
              :readonly="!isAdd || disabledForm.shangjiazhanghao ? true : false"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="商家姓名" prop="shangjiaxingming">
            <el-input
              class="list_inp"
              v-model="form.shangjiaxingming"
              placeholder="商家姓名"
              type="text"
              :readonly="!isAdd || disabledForm.shangjiaxingming ? true : false"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="菜品祥情" prop="caipinxiangqing">
            <editor
              class="list_editor"
              :value="form.caipinxiangqing"
              placeholder="请输入菜品祥情"
              :readonly="!isAdd || disabledForm.caipinxiangqing ? true : false"
              @change="(e) => editorChange(e, 'caipinxiangqing')"
            ></editor>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="formModel_btn_box">
        <el-button class="formModel_cancel" @click="backClick">取消</el-button>
        <el-button class="formModel_confirm" @click="save" type="success">
          保存
        </el-button>
      </div>
    </el-form>
  </div>
</template>
<script setup>
import {
  ref,
  getCurrentInstance,
  watch,
  onUnmounted,
  onMounted,
  nextTick,
  computed,
} from 'vue'
import { useRoute, useRouter } from 'vue-router'
const context = getCurrentInstance()?.appContext.config.globalProperties
const route = useRoute()
const router = useRouter()
//基础信息
const tableName = 'caipinxinxi'
const formName = '菜品信息'
//基础信息
const breadList = ref([
  {
    name: formName,
  },
])
//获取唯一标识
const getUUID = () => {
  return new Date().getTime()
}
//form表单
const form = ref({
  dianpumingcheng: '',
  caipinmingcheng: '',
  caipintupian: '',
  caipinfenlei: '',
  caipinxiangqing: '',
  kouwei: '不辣',
  tuijianzhishu: '',
  yingyangchengfen: '',
  clicktime: '',
  storeupnum: '0',
  clicknum: '0',
  price: '0',
  shangjiazhanghao: '',
  shangjiaxingming: '',
})
const formRef = ref(null)
const id = ref(0)
const type = ref('')
const disabledForm = ref({
  dianpumingcheng: false,
  caipinmingcheng: false,
  caipintupian: false,
  caipinfenlei: false,
  caipinxiangqing: false,
  kouwei: false,
  tuijianzhishu: false,
  yingyangchengfen: false,
  clicktime: false,
  storeupnum: false,
  clicknum: false,
  price: false,
  shangjiazhanghao: false,
  shangjiaxingming: false,
})
const isAdd = ref(false)
//表单验证
//匹配整数
const validateIntNumber = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.isIntNumer(value)) {
    callback(new Error('请输入整数'))
  } else {
    callback()
  }
}
//匹配数字
const validateNumber = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.isNumber(value)) {
    callback(new Error('请输入数字'))
  } else {
    callback()
  }
}
//匹配手机号码
const validateMobile = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.isMobile(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}
//匹配电话号码
const validatePhone = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.isPhone(value)) {
    callback(new Error('请输入正确的电话号码'))
  } else {
    callback()
  }
}
//匹配邮箱
const validateEmail = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.isEmail(value)) {
    callback(new Error('请输入正确的邮箱地址'))
  } else {
    callback()
  }
}
//匹配身份证
const validateIdCard = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.checkIdCard(value)) {
    callback(new Error('请输入正确的身份证号码'))
  } else {
    callback()
  }
}
//匹配网站地址
const validateUrl = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!context?.$toolUtil.isURL(value)) {
    callback(new Error('请输入正确的URL地址'))
  } else {
    callback()
  }
}
const rules = ref({
  dianpumingcheng: [],
  caipinmingcheng: [{ required: true, message: '请输入', trigger: 'blur' }],
  caipintupian: [{ required: true, message: '请输入', trigger: 'blur' }],
  caipinfenlei: [{ required: true, message: '请输入', trigger: 'blur' }],
  caipinxiangqing: [],
  kouwei: [],
  tuijianzhishu: [],
  yingyangchengfen: [],
  clicktime: [],
  storeupnum: [{ validator: validateIntNumber, trigger: 'blur' }],
  clicknum: [{ validator: validateIntNumber, trigger: 'blur' }],
  price: [{ validator: validateNumber, trigger: 'blur' }],
  shangjiazhanghao: [],
  shangjiaxingming: [],
})
//菜品图片上传回调
const caipintupianUploadSuccess = (e) => {
  form.value.caipintupian = e
}
//菜品分类列表
const caipinfenleiLists = ref([])
//口味列表
const kouweiLists = ref([])
//推荐指数列表
const tuijianzhishuLists = ref([])
//methods

//methods
//获取info
const getInfo = () => {
  context
    ?.$http({
      url: `${tableName}/info/${id.value}`,
      method: 'get',
    })
    .then((res) => {
      let reg = new RegExp('../../../file', 'g')
      res.data.data.caipinxiangqing = res.data.data.caipinxiangqing.replace(
        reg,
        '../../../onlineOrder/file'
      )
      form.value = res.data.data
    })
}
const crossRow = ref('')
const crossTable = ref('')
const crossTips = ref('')
const crossColumnName = ref('')
const crossColumnValue = ref('')
//初始化
const init = (
  formId = null,
  formType = 'add',
  formNames = '',
  row = null,
  table = null,
  statusColumnName = null,
  tips = null,
  statusColumnValue = null
) => {
  if (formId) {
    id.value = formId
    type.value = formType
  }
  if (formType == 'add') {
    isAdd.value = true
  } else if (formType == 'info') {
    isAdd.value = false
    getInfo()
  } else if (formType == 'edit') {
    isAdd.value = true
    getInfo()
  } else if (formType == 'cross') {
    isAdd.value = true
    // getInfo()
    for (let x in row) {
      if (x == 'dianpumingcheng') {
        form.value.dianpumingcheng = row[x]
        disabledForm.value.dianpumingcheng = true
        continue
      }
      if (x == 'caipinmingcheng') {
        form.value.caipinmingcheng = row[x]
        disabledForm.value.caipinmingcheng = true
        continue
      }
      if (x == 'caipintupian') {
        form.value.caipintupian = row[x]
        disabledForm.value.caipintupian = true
        continue
      }
      if (x == 'caipinfenlei') {
        form.value.caipinfenlei = row[x]
        disabledForm.value.caipinfenlei = true
        continue
      }
      if (x == 'caipinxiangqing') {
        form.value.caipinxiangqing = row[x]
        disabledForm.value.caipinxiangqing = true
        continue
      }
      if (x == 'kouwei') {
        form.value.kouwei = row[x]
        disabledForm.value.kouwei = true
        continue
      }
      if (x == 'tuijianzhishu') {
        form.value.tuijianzhishu = row[x]
        disabledForm.value.tuijianzhishu = true
        continue
      }
      if (x == 'yingyangchengfen') {
        form.value.yingyangchengfen = row[x]
        disabledForm.value.yingyangchengfen = true
        continue
      }
      if (x == 'clicktime') {
        form.value.clicktime = row[x]
        disabledForm.value.clicktime = true
        continue
      }
      if (x == 'storeupnum') {
        form.value.storeupnum = row[x]
        disabledForm.value.storeupnum = true
        continue
      }
      if (x == 'clicknum') {
        form.value.clicknum = row[x]
        disabledForm.value.clicknum = true
        continue
      }
      if (x == 'price') {
        form.value.price = row[x]
        disabledForm.value.price = true
        continue
      }
      if (x == 'shangjiazhanghao') {
        form.value.shangjiazhanghao = row[x]
        disabledForm.value.shangjiazhanghao = true
        continue
      }
      if (x == 'shangjiaxingming') {
        form.value.shangjiaxingming = row[x]
        disabledForm.value.shangjiaxingming = true
        continue
      }
    }
    if (row) {
      crossRow.value = row
    }
    if (table) {
      crossTable.value = table
    }
    if (tips) {
      crossTips.value = tips
    }
    if (statusColumnName) {
      crossColumnName.value = statusColumnName
    }
    if (statusColumnValue) {
      crossColumnValue.value = statusColumnValue
    }
    form.value.kouwei = '不辣'
    form.value.storeupnum = '0'
    form.value.clicknum = '0'
    form.value.price = '0'
  }
  context
    ?.$http({
      url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
      method: 'get',
    })
    .then((res) => {
      var json = res.data.data
      if (
        json.hasOwnProperty('shangjiazhanghao') &&
        context?.$toolUtil.storageGet('frontRole') != '管理员'
      ) {
        form.value.shangjiazhanghao = json.shangjiazhanghao
        disabledForm.value.shangjiazhanghao = true
      }
      if (
        json.hasOwnProperty('shangjiaxingming') &&
        context?.$toolUtil.storageGet('frontRole') != '管理员'
      ) {
        form.value.shangjiaxingming = json.shangjiaxingming
        disabledForm.value.shangjiaxingming = true
      }
    })
  context
    ?.$http({
      url: `option/caipinfenlei/caipinfenlei`,
      method: 'get',
    })
    .then((res) => {
      caipinfenleiLists.value = res.data.data
    })
  kouweiLists.value = '微辣,加辣,不辣'.split(',')
  tuijianzhishuLists.value = '★,★★,★★★,★★★★,★★★★★'.split(',')
}
//初始化
//取消
const backClick = () => {
  history.back()
}
//富文本数据回调
const editorChange = (e, name) => {
  form.value[name] = e
}
//提交
const save = () => {
  if (form.value.caipintupian != null) {
    form.value.caipintupian = form.value.caipintupian.replace(
      new RegExp(context?.$config.url, 'g'),
      ''
    )
  }
  var table = crossTable.value
  var objcross = JSON.parse(JSON.stringify(crossRow.value))
  let crossUserId = ''
  let crossRefId = ''
  let crossOptNum = ''
  if (type.value == 'cross') {
    if (crossColumnName.value != '') {
      if (!crossColumnName.value.startsWith('[')) {
        for (let o in objcross) {
          if (o == crossColumnName.value) {
            objcross[o] = crossColumnValue.value
          }
        }
        //修改跨表数据
        changeCrossData(objcross)
      } else {
        crossUserId = context?.$toolUtil.storageGet('userid')
        crossRefId = objcross['id']
        crossOptNum = crossColumnName.value.replace(/\[/, '').replace(/\]/, '')
      }
    }
  }
  formRef.value.validate((valid) => {
    if (valid) {
      if (crossUserId && crossRefId) {
        form.value.crossuserid = crossUserId
        form.value.crossrefid = crossRefId
        let params = {
          page: 1,
          limit: 1000,
          crossuserid: form.value.crossuserid,
          crossrefid: form.value.crossrefid,
        }
        context
          ?.$http({
            url: `${tableName}/page`,
            method: 'get',
            params: params,
          })
          .then((res) => {
            if (res.data.data.total >= crossOptNum) {
              context?.$toolUtil.message(`${crossTips.value}`, 'error')
              return false
            } else {
              context
                ?.$http({
                  url: `${tableName}/${!form.value.id ? 'save' : 'update'}`,
                  method: 'post',
                  data: form.value,
                })
                .then((res) => {
                  context?.$toolUtil.message(`操作成功`, 'success', () => {
                    history.back()
                  })
                })
            }
          })
      } else {
        context
          ?.$http({
            url: `${tableName}/${!form.value.id ? 'save' : 'update'}`,
            method: 'post',
            data: form.value,
          })
          .then((res) => {
            context?.$toolUtil.message(`操作成功`, 'success', () => {
              history.back()
            })
          })
      }
    }
  })
}
//修改跨表数据
const changeCrossData = (row) => {
  context
    ?.$http({
      url: `${crossTable.value}/update`,
      method: 'post',
      data: row,
    })
    .then((res) => {})
}
onMounted(() => {
  type.value = route.query.type ? route.query.type : 'add'
  let row = null
  let table = null
  let statusColumnName = null
  let tips = null
  let statusColumnValue = null
  if (type.value == 'cross') {
    row = context?.$toolUtil.storageGet('crossObj')
      ? JSON.parse(context?.$toolUtil.storageGet('crossObj'))
      : {}
    table = context?.$toolUtil.storageGet('crossTable')
    statusColumnName = context?.$toolUtil.storageGet('crossStatusColumnName')
    tips = context?.$toolUtil.storageGet('crossTips')
    statusColumnValue = context?.$toolUtil.storageGet('crossStatusColumnValue')
  }
  init(
    route.query.id ? route.query.id : null,
    type.value,
    '',
    row,
    table,
    statusColumnName,
    tips,
    statusColumnValue
  )
})
</script>
<style lang="scss" scoped>
// 面包屑盒子
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
// 表单
.add_form {
  border: 0px solid #27bacc30;
  border-radius: 0px;
  padding: 30px 7%;
  background: none;
  // form item
  :deep(.el-form-item) {
    border: 0px solid #eee;
    padding: 6px 0;
    margin: 0 0 20px 0;
    background: none;
    display: flex;
    //label
    .el-form-item__label {
      background: none;
      display: block;
      width: auto;
      min-width: 150px;
      text-align: right;
    }
    // 内容盒子
    .el-form-item__content {
      display: flex;
      width: calc(100% - 150px);
      justify-content: flex-start;
      align-items: center;
      flex-wrap: wrap;
      // 输入框
      .list_inp {
        padding: 0 10px;
        background: #eee;
        width: 50%;
        border-color: #eee;
        border-width: 1px;
        line-height: 36px;
        box-sizing: border-box;
        border-style: solid;
        min-width: 350px;
        height: 36px;
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
      // 下拉框
      .list_sel {
        border-radius: 0px;
        padding: 0 10px;
        background: #eee;
        width: 50%;
        border-color: #eee;
        border-width: 1px;
        line-height: 36px;
        box-sizing: border-box;
        border-style: solid;
        min-width: 300px;
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
      // 富文本
      .list_editor {
        border-radius: 0;
        padding: 0;
        margin: 0;
        background: #eee;
        width: 99%;
        min-height: 250px;
        border-color: #eee;
        border-width: 1px;
        border-style: solid;
        height: auto;
      }
      //图片上传样式
      .el-upload-list {
        //提示语
        .el-upload__tip {
          margin: 7px 0 0;
          color: #999;
          display: flex;
          font-size: 14px;
          justify-content: flex-start;
          align-items: center;
        }
        //外部盒子
        .el-upload--picture-card {
          border: 1px solid #eee;
          cursor: pointer;
          border-radius: 2px;
          background: #eee;
          width: 120px;
          line-height: 90px;
          text-align: center;
          height: 80px;
          //图标
          .el-icon {
            color: #999;
            font-size: 24px;
          }
        }
        .el-upload-list__item {
          border: 1px solid #eee;
          cursor: pointer;
          border-radius: 2px;
          background: #eee;
          width: 120px;
          line-height: 90px;
          text-align: center;
          height: 80px;
        }
      }
    }
  }
}
// 按钮盒子
.formModel_btn_box {
  padding: 0 150px;
  display: flex;
  width: 100%;
  justify-content: center;
  align-items: center;
  .formModel_cancel {
    border: 0px solid #ccc;
    cursor: pointer;
    border-radius: 2px;
    padding: 0 40px;
    margin: 0 20px 0 0;
    color: #fff;
    background: #fb9203;
    width: auto;
    font-size: 14px;
    line-height: 40px;
    height: 40px;
  }
  .formModel_cancel:hover {
  }

  .formModel_confirm {
    border: 0;
    cursor: pointer;
    border-radius: 2px;
    padding: 0 40px;
    margin: 0 20px 0 0;
    color: #fff;
    background: #9fc33d;
    width: auto;
    font-size: 14px;
    line-height: 40px;
    height: 40px;
  }
  .formModel_confirm:hover {
  }
}
</style>
