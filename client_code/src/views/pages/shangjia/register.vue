<template>
  <div>
    <div class="register_view">
      <div class="outTitle_view">
        <div class="outTilte">{{ projectName }}注册</div>
      </div>
      <el-form :model="registerForm" class="register_form">
        <div class="list_item">
          <div class="list_label">商家账号：</div>
          <input
            class="list_inp"
            v-model="registerForm.shangjiazhanghao"
            placeholder="请输入商家账号"
            type="text"
          />
        </div>
        <div class="list_item">
          <div class="list_label">密码：</div>
          <input
            class="list_inp"
            v-model="registerForm.mima"
            placeholder="请输入密码"
            type="password"
          />
        </div>
        <div class="list_item">
          <div class="list_label">确认密码：</div>
          <input
            class="list_inp"
            v-model="registerForm.mima2"
            type="password"
            placeholder="请输入确认密码"
          />
        </div>
        <div class="list_item">
          <div class="list_label">商家姓名：</div>
          <input
            class="list_inp"
            v-model="registerForm.shangjiaxingming"
            placeholder="请输入商家姓名"
            type="text"
          />
        </div>
        <div class="list_item">
          <div class="list_label">营业执照：</div>
          <div class="list_file_list">
            <uploads
              action="file/upload"
              tip="请上传营业执照"
              :limit="3"
              style="width: 100%; text-align: left"
              :fileUrls="
                registerForm.yingyezhizhao ? registerForm.yingyezhizhao : ''
              "
              @change="yingyezhizhaoUploadSuccess"
            >
            </uploads>
          </div>
        </div>
        <div class="list_item">
          <div class="list_label">手机：</div>
          <input
            class="list_inp"
            v-model="registerForm.shouji"
            placeholder="请输入手机"
            type="text"
          />
        </div>
        <div class="list_btn">
          <el-button class="register" type="success" @click="handleRegister"
            >注册</el-button
          >
          <div class="r-login" @click="close">已有账号，直接登录</div>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance, nextTick } from 'vue'
const context = getCurrentInstance()?.appContext.config.globalProperties
const projectName = context?.$project.projectName
//获取注册类型
import { useRoute } from 'vue-router'
const route = useRoute()
const tableName = ref('shangjia')

//公共方法
const getUUID = () => {
  return new Date().getTime()
}

const registerForm = ref({
  sfsh: '待审核',
})
const init = () => {}
const yingyezhizhaoUploadSuccess = (fileUrls) => {
  registerForm.value.yingyezhizhao = fileUrls
}
// 多级联动参数
//注册按钮
const handleRegister = () => {
  let url = tableName.value + '/register'
  if (!registerForm.value.shangjiazhanghao) {
    context?.$toolUtil.message(`商家账号不能为空`, 'error')
    return false
  }
  if (!registerForm.value.mima) {
    context?.$toolUtil.message(`密码不能为空`, 'error')
    return false
  }
  if (registerForm.value.mima != registerForm.value.mima2) {
    context?.$toolUtil.message('两次密码输入不一致', 'error')
    return false
  }
  if (!registerForm.value.shangjiaxingming) {
    context?.$toolUtil.message(`商家姓名不能为空`, 'error')
    return false
  }
  if (registerForm.value.yingyezhizhao != null) {
    registerForm.value.yingyezhizhao = registerForm.value.yingyezhizhao.replace(
      new RegExp(context?.$config.url, 'g'),
      ''
    )
  }
  if (
    registerForm.value.shouji &&
    !context?.$toolUtil.isMobile(registerForm.value.shouji)
  ) {
    context?.$toolUtil.message(`手机应输入手机格式`, 'error')
    return false
  }

  context
    ?.$http({
      url: url,
      method: 'post',
      data: registerForm.value,
    })
    .then((res) => {
      context?.$toolUtil.message('注册成功', 'success', (obj) => {
        context?.$router.push({
          path: '/login',
        })
      })
    })
}
//返回登录
const close = () => {
  context?.$router.push({
    path: '/login',
  })
}
init()
</script>
<style lang="scss" scoped>
.register_view {
  background-repeat: no-repeat;
  flex-direction: column;
  background: url(http://localhost:8080/onlineOrder/file/bef054677d69402bbf0523a6dcc3be42.jpg);
  display: flex;
  font-size: 100% 100%;
  min-height: 100vh;
  justify-content: center;
  align-items: center;
  position: relative;
  background-position: center center;
  // 标题盒子
  .outTitle_view {
    border-radius: 20px 20px 0 0;
    padding: 0px;
    margin: 20px 0 0;
    background: rgba(255, 255, 255, 0.9);
    display: flex;
    width: 540px;
    border-color: #b4d2ae;
    border-width: 0;
    justify-content: center;
    border-style: groove;
    height: 54px;
    .outTilte {
      border: 0px solid #fff;
      border-radius: 0px;
      padding: 0px 20px;
      color: #333;
      font-size: 20px;
      line-height: 78px;
    }
  }
  // 表单盒子
  .register_form {
    border-radius: 30px;
    padding: 40px 80px 30px 40px;
    box-shadow: 0px 26px 26px -30px #999;
    margin: 0;
    background: rgba(255, 255, 255, 0.9);
    display: block;
    width: 670px;
    border-color: #b4d2ae;
    border-width: 0px;
    justify-content: center;
    border-style: groove;
    flex-wrap: wrap;
  }
  // item盒子
  .list_item {
    margin: 0 0 12px;
    display: flex;
    width: 100%;
    justify-content: flex-start;
    align-items: center;
    // label
    .list_label {
      color: #333;
      width: 120px;
      font-size: 14px;
      box-sizing: border-box;
      text-align: right;
    }
    // 输入框
    :deep(.list_inp) {
      border: 1px solid #ddd;
      border-radius: 4px;
      padding: 0 10px;
      width: calc(100% - 100px);
      line-height: 36px;
      box-sizing: border-box;
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
  }
  //图片上传样式
  .list_file_list {
    //提示语
    :deep(.el-upload__tip) {
      margin: 7px 0 0;
      color: #9fc33d;
      display: flex;
      font-size: 14px;
      justify-content: flex-start;
      align-items: center;
    }
    //外部盒子
    :deep(.el-upload--picture-card) {
      border: 1px solid #ddd;
      cursor: pointer;
      background-color: #fff;
      border-radius: 4px;
      width: 160px;
      line-height: 70px;
      text-align: center;
      height: 60px;
      //图标
      .el-icon {
        color: #999;
        font-size: 24px;
      }
    }
    :deep(.el-upload-list__item) {
      border: 1px solid #ddd;
      cursor: pointer;
      background-color: #fff;
      border-radius: 4px;
      width: 160px;
      line-height: 70px;
      text-align: center;
      height: 60px;
    }
  }
  //按钮盒子
  .list_btn {
    margin: 20px 0 0;
    display: flex;
    width: 100%;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    //注册按钮
    .register {
      border: none;
      border-radius: 20px;
      box-shadow: inset 0px 0px 26px 0px #9fc33d;
      margin: 0 20px;
      color: #333;
      background: #fff;
      width: 120px;
      font-size: 16px;
      height: 40px;
    }
    //注册按钮悬浮样式
    .register:hover {
      border: none;
      border-radius: 4px;
      margin: 0 20px;
      background: rgba(255, 153, 0, 1);
      width: 120px;
      font-size: 16px;
      opacity: 1;
      height: 40px;
    }
    //已有账号
    .r-login {
      cursor: pointer;
      padding: 01;
      color: #666;
      width: auto;
      font-size: 14px;
      text-align: right;
    }
  }
}
</style>
