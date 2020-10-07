<template>
  <div>
    <van-nav-bar title="我的预约" left-text="返回" @click-left="onClickLeft" />

    <div v-for="item in list" v-bind:key="item.id">
      <div v-if="item.canceled === 0">
        <van-swipe-cell>
          <van-cell
            :title="`${item.bookDate} 周${item.bookWeek}`"
            :label="`医师: ${item.doctor}`"
            :value="`${item.timeRange}`"
            style="text-align: left"
          />

          <template #right>
            <van-button
              square
              text="取消"
              type="danger"
              style="height: 100%"
              :loading="deleteLoading"
              @click="deleteInfo(item)"
            />
          </template>
        </van-swipe-cell>
      </div>
      <div v-else>
        <van-cell
          :title="`${item.bookDate} 周${item.bookWeek}`"
          :label="`医师: ${item.doctor}`"
          :value="`${item.timeRange}`"
          style="text-align: left; background:#d9d9d9;color:#bfbfbf;"
        />
      </div>
    </div>

    <van-pagination
      v-model="page"
      :total-items="total"
      :items-per-page="size"
      @change="loadData"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      page: 1,
      size: 20,
      total: 0,
      list: [],
      deleteLoading: false,
      today: new Date()
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      const url = `/api/user/book-info?page=${this.page}&size=${this.size}`;
      let toast = this.$toast.loading("加载中");
      this.$h
        .get(url)
        .cb((data) => {
          if (data["status"] === 0) {
            this.list = data["list"];
            this.total = data["total"];
          } else {
            this.$notify({
              type: "danger",
              message: decodeURI(data["message"]),
            });
          }
        })
        .fcb(() => toast.clear())
        .req();
    },
    onClickLeft() {
      this.$router.push("/");
    },
    deleteInfo(item) {
      const url = `/api/user/book-info?id=${item.id}`;
      this.deleteLoading = true;
      this.$h.delete(url)
      .cb(data => {
        if (data['status'] === 0) {
          item.canceled = 1;
          this.$notify({type: 'success', message: '已取消'});
        } else {
          this.$notify({type: 'success', message: decodeURI(data['message'])});
        }
      })
      .fcb(() => this.loading = false)
      .req();
    },
  },
};
</script>

<style>
</style>