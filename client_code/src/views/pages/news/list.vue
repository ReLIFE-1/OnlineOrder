<template>
	<div class="app-contain" :style='{"padding":"0 10px","margin":"0 auto 20px","overflow":"hidden","borderRadius":"0px","background":"#fff","width":"1200px","position":"relative"}'>
		<div class="back_view" v-if="centerType">
			<el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
		</div>
		<div class="section_title">
			{{formName}}
		</div>
		<el-form inline :model="searchQuery" class="list_search">
			<div class="search_view">
				<div class="search_label">
					标题：
				</div>
				<div class="search_box">
					<el-input v-model="searchQuery.title" placeholder="标题" clearable></el-input>
				</div>
			</div>
			<div class="search_btn_view">
				<el-button class="search_btn" type="primary" @click="searchClick">搜索</el-button>
			</div>
		</el-form>
		<div class="news_list_four">
			<div class="news_left">
				<template v-for="(item,index) in list" :key="index">
					<div class="four_left_item animation_box" v-if="index>=Number(4)" @click.stop="newsDetail(item.id)">
						<div class="time_box">
							<div class="date">{{item.addtime.split(' ')[0].split('-')[1]}}-{{item.addtime.split(' ')[0].split('-')[2]}}</div>
							<div class="year">{{item.addtime.split(' ')[0].split('-')[0]}}</div>
						</div>
						<div class="content">
							<div class="news_title">{{item.title}}</div>
							<div class="news_text">{{item.introduction}}</div>
						</div>
					</div>
				</template>
			</div>
			<div class="news_right">
				<template v-for="(item,index) in list" :key="index">
					<div class="four_right_item animation_box" v-if="index<Number(4)" @click.stop="newsDetail(item.id)">
						<div class="img_box">
							<img :src="item.picture?$config.url + item.picture :''" alt="">
						</div>
						<div class="content">
							<div class="news_title">{{item.title}}</div>
						</div>
					</div>
				</template>
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
			:style='{"border":"1px solid #ddd","padding":"4px 0","margin":"10px 0 20px","whiteSpace":"nowrap","color":"#333","textAlign":"center","flexWrap":"wrap","background":"linear-gradient(270deg, rgba(238,238,238,1) 0%, rgba(255,255,255,1) 50%, rgba(238,238,238,1) 100%)","display":"flex","width":"100%","fontWeight":"500","justifyContent":"center"}'
			@size-change="sizeChange"
			@current-change="currentChange" 
			@prev-click="prevClick"
			@next-click="nextClick"  />
		
		
		<formModel ref="formModelRef"></formModel>
	</div>
</template>

<script setup>
	import formModel from './formModel'
	import {
		ref,
		nextTick,
		getCurrentInstance
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	//基础信息
	const tableName = 'news'
	const formName = '公告信息'
	const router = useRouter()
	const route = useRoute()
	//基础信息
	//权限验证
	const btnAuth = (e, a) => {
		return context?.$toolUtil.isAuth(e, a)
	}
	const list = ref([])
	const listLoading = ref(false)
	const listQuery = ref({
		page: 1,
		limit: 20,
		sort: 'addtime',
		order: 'desc'
	})
	const searchQuery = ref({})
	//分页
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const total = ref(0)
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
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		if (searchQuery.value.title && searchQuery.value.title != '') {
			params['title'] = `%${searchQuery.value.title}%`
		}
		context?.$http({
			url: `news/list`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = res.data.data.total
		})
	}
	//判断是否从个人中心跳转
	const centerType = ref(false)
	//返回
	const backClick = () => {
		router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
	}
	const init = () => {
		if (route.query.centerType) {
			centerType.value = true
		}
		getList()
	}
	//定义弹窗
	const formModelRef = ref(null)
	const newsDetail = (id = null) => {
		if (id) {
			formModelRef.value.init(id)
		}
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
					.el-input__wrapper{
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
			}
			// 搜索按钮-悬浮
			.search_btn:hover {
			}
		}
	}
	.news_list_four {
		padding: 0;
		margin: 40px 0 0;
		display: flex;
		width: 100%;
		justify-content: space-between;
		flex-wrap: wrap;
		.news_left {
			flex-direction: column;
			display: flex;
			width: 100%;
			justify-content: space-between;
			order: 2;
			.four_left_item{
				cursor: pointer;
				padding: 0 0 10px;
				margin: 0 0 20px;
				display: flex;
				width: 100%;
				border-color: #eee0b3;
				border-width: 0 0 4px;
				justify-content: space-between;
				align-items: center;
				border-style: double;
				.time_box {
					flex-direction: row;
					background: none;
					display: flex;
					width: 130px;
					justify-content: flex-end;
					align-items: flex-end;
					order: 22;
					.year{
						padding: 0px 0 0;
						margin: 0 5px 0 0;
						color: #666;
						font-weight: 600;
						font-size: 16px;
						line-height: 1;
					}
					.date{
						margin: 0 10px 0 0;
						color: #999;
						font-weight: 500;
						font-size: 16px;
						line-height: 1;
						order: 2;
					}
				}
			}
			.content{
				margin: 0 0 0 10px;
				width: calc(100% - 130px);
				.news_title{
					overflow: hidden;
					white-space: nowrap;
					font-weight: bold;
					width: 100%;
					text-overflow: ellipsis;
				}
				.news_text{
					padding: 10px 0 0;
					overflow: hidden;
					display: -webkit-box;
					text-overflow: ellipsis;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
				}
			}
			
		}
		.news_right {
			margin: 0 0px 0 0;
			display: flex;
			width: calc(100% - 0px);
			justify-content: space-between;
			order: 1;
			.four_right_item {
				width: 23%;
				.img_box {
				width: 100%;
				font-size: 0;
				height: 200px;
					img {
				cursor: pointer;
				border-radius: 20px;
				object-fit: cover;
				width: 100%;
				height: 100%;
					}
				}
				.content {
					width: 100%;
					text-align: center;
					.news_title {
						cursor: pointer;
						overflow: hidden;
						white-space: nowrap;
						font-weight: 600;
						width: 100%;
						font-size: 14px;
						line-height: 30px;
						text-overflow: ellipsis;
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
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(10px, 0px, 0px);
		-webkit-perspective: 1000px;
		perspective: 1000px;
		transition: 0.3s;
	}
	.animation_box img {
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		z-index: initial;
	}
	.animation_box img:hover {
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
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
			color: #C0C4CC;
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
			color: #C0C4CC;
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
				border: 0px solid #DCDFE6;
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
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;
						.el-input__wrapper{
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
				.el-input__wrapper{
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
</style>