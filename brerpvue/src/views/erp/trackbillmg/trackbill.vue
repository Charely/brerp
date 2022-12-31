<template>
    <a-card>
        <div class="app-container">
      <div><h1>创建矩形</h1></div>
      <div id="container" ref="container" class="container" />
      <div class="page_but">
        <el-row>
          <el-col>
            <el-button type="primary" @click="addDlock(rect1)">创建矩形1</el-button>
          </el-col>
        </el-row>
      </div>
    </div>
    </a-card>
    
  </template>
  
  <script>
  import { konva } from '@/mixins'
  import Konva from 'konva'
  
  export default {
    name: 'trackbill',
    mixins: [konva],
    data() {
      return {
        rect1: {
          x: 5,
          y: 5,
          width: 100,
          height: 50,
          fill: '',
          stroke: 'black',
          strokeWidth: 1
        }
      }
    },
    methods: {
      /**
       * 按钮--创建一个矩形 yyshu 20201031
       * @param rect 矩形属性
       */
      addDlock(rect) {
        const layer = this.pageLayer[0].layer
        const rect1 = new Konva.Rect(rect)

        const rect2 = {
          x: 205,
          y: 5,
          width: 100,
          height: 50,
          fill: '',
          stroke: 'black',
          strokeWidth: 1
        }

        const rect2_2 = new Konva.Rect(rect2)
        layer.add(rect1)
        layer.add(rect2_2)


        const lineAct={
            x: 105,
            y: 30,
            points: [0, 0, 100, 0],
            pointerLength: 10,
            pointerWidth: 10,
            fill: 'black',
            stroke: 'black',
            strokeWidth: 2
        }

        const arr=new Konva.Arrow(lineAct)
        layer.add(arr);
        layer.batchDraw()
        console.log('创建矩形成功', rect1)
      }
    }
  }
  </script>
  