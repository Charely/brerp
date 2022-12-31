import Konva from 'konva'

/**
 * KONVA
 */
export default {
  data() {
    return {
      // canvas宽度
      canWidth: 0,
      // 页面canvas
      pageStage: null,
      // canvas的图层列表
      pageLayer: []
    }
  },
  created() {
    // 初始化--创建画布并创建一个图层
    setTimeout(() => {
      this.init()
    }, 10)
  },
  methods: {
    /**
     * 生命周期--初始化
     */
    init() {
      const main = this.$refs.container
      this.canWidth = main.offsetWidth
      this.pageStage = new Konva.Stage({
        container: 'container',
        width: main.offsetWidth,
        height: 440
      })
      this.addLayer()
    },
    /**
     * 按钮--新增图层
     */
    addLayer() {
      const name = this.getTimestamp('l_')
      const layer = new Konva.Layer()
      this.pageLayer.push({ name, layer })
      this.pageStage.add(layer)
      this.$message({
        message: `${name} 图层添加成功`,
        type: 'success'
      })
    },
    /**
     * 按钮--清空画布 yyshu 20201031
     * 注:因为目前只用了一层画布,默认清空第一层
     * 目前不好用,在找原因
     */
    clearDraw() {
      // const layer = this.pageLayer[0].layer
      // layer.clear()
      // layer.clearCache()
      // layer.batchDraw()
      console.log('触发完成')
    }
  }
}
