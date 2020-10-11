<template>
  <div>
    <van-nav-bar title="系统设置" left-text="返回" @click-left="onClickLeft" />

    <van-form>
      <van-field
        v-for="(item, index) in list"
        v-bind:key="index"
        v-model="item.confValue"
        center
        clearable
        :label="item.confTitle"
        :placeholder="item.confComment"
      >
        <template #button>
          <van-button @click="modify(item)" native-type="button" size="small" type="primary">修改</van-button>
        </template>
      </van-field>
    </van-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      list: [],
      loading: false
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    onClickLeft() {
      this.$router.push("/system");
    },
    loadData() {
      const url = `/api/system/system-setting`;
      this.$h
        .get(url)
        .cb((data) => {
          if (data.status === 0) {
            this.list = data.list;
          } else {
            this.$notify({ type: "danger", message: decodeURI(data.message) });
          }
        })
        .req();
    },
    modify(item) {
        console.log('modify', item);
        const url = `/api/system/update-setting`
        const fd = new FormData();
        fd.append('key', item.confKey);
        fd.append('value', item.confValue);
        let toast = this.$toast.loading('正在修改');
        this.$h.post(url, fd)
        .cb(data => {
            if (data.status === 0) {
                this.$notify({type: 'success', message: '已修改'})
            } else {
                this.$notify({type: 'danger', message: decodeURI(data.message)})
            }
        })
        .fcb(() => toast.clear())
        .req();
        
    }
  },
};
</script>

<style>
</style>