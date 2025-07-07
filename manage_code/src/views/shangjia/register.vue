<template>
  <div>
    <div class="register_view">
      <el-form :model="registerForm" class="register_form">
        <div class="title_view">{{ projectName }}注册</div>
        <div class="list_item">
          <div class="list_label">商家账号：</div>
          <el-input
            class="list_inp"
            v-model="registerForm.shangjiazhanghao"
            placeholder="请输入商家账号"
            type="text"
          />
        </div>
        <div class="list_item">
          <div class="list_label">密码：</div>
          <el-input
            class="list_inp"
            v-model="registerForm.mima"
            placeholder="请输入密码"
            type="password"
          />
        </div>
        <div class="list_item">
          <div class="list_label">确认密码：</div>
          <el-input
            class="list_inp"
            v-model="registerForm.mima2"
            type="password"
            placeholder="请输入确认密码"
          />
        </div>
        <div class="list_item">
          <div class="list_label">商家姓名：</div>
          <el-input
            class="list_inp"
            v-model="registerForm.shangjiaxingming"
            placeholder="请输入商家姓名"
            type="text"
          />
        </div>
        <div class="list_item">
          <div class="list_label">营业执照：</div>
          <div
            :style="{ width: '100%', margin: '0 0 0 10px', flex: '1' }"
            class="list_file_list"
          >
            <uploads
              action="file/upload"
              tip="请上传营业执照"
              :limit="3"
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
          <el-input
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
  background-size: 100% 100% !important;
  background: url(http://localhost:8080/onlineOrder/file/f560a0f6ba574905b6b9e225ea66d64a.jpg);
  display: flex;
  min-height: 100vh;
  justify-content: center;
  align-items: center;
  position: relative;
  background-position: center center;
  // 表单盒子
  .register_form {
    border: 10px solid #fff;
    border-radius: 0 0 30px 30px;
    padding: 0 20px 20px 0;
    box-shadow: inset 0px 0px 120px 0px #ddd, 0px 10px 20px #333;
    margin: 0 auto;
    flex-direction: column;
    background: #fff;
    display: flex;
    width: 600px;
    justify-content: flex-start;
    flex-wrap: wrap;
  }
  // 标题样式
  .title_view {
    padding: 0px;
    margin: 30px auto 30px;
    color: #333;
    font-weight: 600;
    width: 90%;
    font-size: 22px;
    text-align: center;
  }
  // item盒子
  .list_item {
    margin: 0 auto 20px;
    display: flex;
    width: 90%;
    justify-content: flex-start;
    align-items: center;
    // label
    .list_label {
      color: #666;
      background: none;
      width: 120px;
      font-size: 14px;
      line-height: 36px;
      box-sizing: border-box;
      text-align: right;
    }
    // 输入框
    :deep(.list_inp) {
      border: 1px solid #7da06630;
      border-radius: 0 0 20px 20px;
      padding: 0 10px;
      color: #666;
      background: #7da06610;
      flex: 1;
      width: 100%;
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
  //按钮盒子
  .list_btn {
    margin: 10px auto;
    display: flex;
    width: 80%;
    align-items: center;
    flex-wrap: wrap;
    //注册按钮
    .register {
      border: 0px solid #ff9900;
      border-radius: 0 0 20px 20px;
      padding: 0 20px;
      margin: 0 auto;
      color: #fff;
      background: #7ca065;
      width: auto;
      font-size: 16px;
      min-width: 120px;
      height: 40px;
    }
    //注册按钮悬浮样式
    .register:hover {
      border: 1px solid #ff9900;
      border-radius: 0;
      padding: 0 24px;
      margin: 0 auto;
      color: #990033;
      background: linear-gradient(
        0deg,
        rgba(255, 255, 255, 1) 0%,
        rgba(251, 192, 102, 1) 100%
      );
      width: auto;
      font-size: 16px;
      height: 40px;
    }
    //已有账号
    .r-login {
      cursor: pointer;
      padding: 10px 0 0;
      color: #999;
      width: 100%;
      font-size: 14px;
      text-align: right;
    }
  }
  //图片上传样式
  .list_file_list {
    //提示语
    :deep(.el-upload__tip) {
      margin: 7px 0 0;
      color: #999;
      display: flex;
      font-size: 14px;
      justify-content: flex-start;
      align-items: center;
    }
    //外部盒子
    :deep(.el-upload--picture-card) {
      border: 1px solid #7da06630;
      cursor: pointer;
      background-color: #7da06610;
      border-radius: 0 0 20px 20px;
      width: 100px;
      line-height: 68px;
      text-align: center;
      height: 60px;
      //图标
      .el-icon {
        color: #7ca065;
        font-size: 26px;
      }
    }
    :deep(.el-upload-list__item) {
      border: 1px solid #7da06630;
      cursor: pointer;
      background-color: #7da06610;
      border-radius: 0 0 20px 20px;
      width: 100px;
      line-height: 68px;
      text-align: center;
      height: 60px;
    }
  }
}
</style>
