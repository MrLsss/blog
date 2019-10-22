new Vue({
    el: '#container',
    data: {
        articleCount: 0,
        tagCount: 0,
        classifyCount: 0,
        msgboardCount: 0,
        classPie: [],
        top10: [],
        tagsCloud: [],
        index: 1,
        record: [
        ]
    },
    mounted() { // 页面已经渲染完成

    },
    created() { // 初始化数据
        this.init();
    },
    computed: { //计算属性

    },
    methods: {
        //初始化数据
        init() {
            var _this = this;
            axios.get(api.index.count).then(function (res) {
                _this.articleCount = res.data.data.article;
                _this.tagCount = res.data.data.tag;
                _this.classifyCount = res.data.data.classify;
                _this.msgboardCount = res.data.data.comment;
                _this.$options.methods.initCount(_this.articleCount, 'articleCount');
                _this.$options.methods.initCount(_this.tagCount, 'tagCount');
                _this.$options.methods.initCount(_this.classifyCount, 'classifyCount');
                _this.$options.methods.initCount(_this.msgboardCount, 'msgboardCount');
            });

            axios.get(api.index.pie).then(function (res) {
                _this.classPie = res.data.data;
                _this.$options.methods.initClassPie(_this.classPie);
            });

            axios.get(api.index.top10).then(function (res) {
                _this.top10 = res.data.data;
                _this.$options.methods.initTop10(_this.top10);
            });

            axios.get(api.index.cloud).then(function (res) {
                _this.tagsCloud = res.data.data;
                _this.$options.methods.initTagsCloud(_this.tagsCloud);
            });

            axios.get(api.index.operationTime).then(function (res) {
                _this.record = res.data.data;
            });
        },
        initCount(count, className) {
            var div_by = 100,
                speed = Math.round(count / div_by),
                $display = $('.' + className),
                run_count = 1,
                int_speed = 24;
            var int = setInterval(function () {
                if (run_count < div_by) {
                    $display.text(speed * run_count);
                    run_count++;
                } else if (parseInt($display.text()) < count) {
                    var curr_count = parseInt($display.text()) + 1;
                    $display.text(curr_count);
                } else {
                    clearInterval(int);
                }
            }, int_speed);
        },
        initClassPie(classPie) {
            var chart = new G2.Chart({
                container: 'articleClassifyPie',
                forceFit: true
            });
            chart.source(classPie, {
                percent: {
                    formatter: function formatter(val) {
                        val = val * 100 + '%';
                        return val;
                    }
                }
            });
            chart.coord('theta');
            chart.tooltip({
                showTitle: false
            });
            chart.intervalStack().position('percent').color('item').label('percent', {
                offset: -40,
                // autoRotate: false,
                textStyle: {
                    textAlign: 'center',
                    shadowBlur: 2
                }
            }).tooltip('item*percent', function (item, percent) {
                percent = percent * 100 + '%';
                return {
                    name: item,
                    value: percent
                };
            }).style({
                lineWidth: 1,
                stroke: '#fff'
            });
            chart.render();
        },
        initTop10(top10) {
            var chart = new G2.Chart({
                container: 'visitTop',
                forceFit: true,
                padding: [20, 40, 50, 124]
            });
            var dv = new DataSet.View().source(top10);
            var range = dv.range('value');
            var max = range[1];
            chart.source(top10, {
                value: {
                    max: max,
                    min: 0,
                    nice: false,
                    alias: '访问量'
                }
            });
            chart.axis('type', {
                label: {
                    textStyle: {
                        fill: '#000',
                        fontSize: 12
                    }
                },
                tickLine: {
                    alignWithLabel: false,
                    length: 0
                },
                line: {
                    lineWidth: 0
                }
            });
            chart.axis('value', {
                label: null,
                title: {
                    offset: 30,
                    textStyle: {
                        fontSize: 12,
                        fontWeight: 300
                    }
                }
            });
            chart.legend(false);
            chart.coord().transpose();
            chart.interval().position('type*value').size(26).opacity(1).label('value', {
                textStyle: {
                    fill: '#000'
                },
                offset: 10
            });
            chart.render();
        },
        initTagsCloud(cloud) {
            function getTextAttrs(cfg) {
                return _.assign({}, cfg.style, {
                    fillOpacity: cfg.opacity,
                    fontSize: cfg.origin._origin.size,
                    rotate: cfg.origin._origin.rotate,
                    text: cfg.origin._origin.text,
                    textAlign: 'center',
                    fontFamily: cfg.origin._origin.font,
                    fill: cfg.color,
                    textBaseline: 'Alphabetic'
                });
            }

            G2.Shape.registerShape('point', 'cloud', {
                drawShape: function drawShape(cfg, container) {
                    var attrs = getTextAttrs(cfg);
                    return container.addShape('text', {
                        attrs: _.assign(attrs, {
                            x: cfg.x,
                            y: cfg.y
                        })
                    });
                }
            });
            var dv = new DataSet.View().source(cloud);
            var range = dv.range('value');
            console.log(range);
            var min = range[0];
            var max = range[1];


            dv.transform({
                type: 'tag-cloud',
                fields: ['x', 'value'],
                font: 'Verdana',
                padding: 10,
                timeInterval: 5000, // max execute time
                rotate: function rotate() {
                    return 0;
                },
                fontSize: function fontSize(d) {
                    if (d.value) {
                        return (d.value - min) / (max - min) * (60 - 24) + 24;
                    }
                    return 0;
                }
            });
            var chart = new G2.Chart({
                container: 'tagsCloud',
                width: 500,
                height: 500,
                padding: 0
            });
            chart.source(dv, {
                x: {
                    nice: false
                },
                y: {
                    nice: false
                }
            });
            chart.legend(false);
            chart.axis(false);
            chart.tooltip({
                showTitle: false
            });
            chart.coord().reflect();
            chart.point().position('x*y').color('value').shape('cloud');
            chart.render();
        },
        setting() {
            window.location.href = '/admin/setting';
        }
    }
})