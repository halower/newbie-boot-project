$(function () {
    let tryItOutPaths = [];
    let isProd = false;
    $.ajax({
        url: 'api/swagger/tio',
        method: 'get',
        async: false,
        success: function (res) {
            tryItOutPaths = res.data || [];
            isProd = res.env != 'dev';
        }
    })

    // 初始化如果已经存在按钮则进行过滤操作
    let task1 = setInterval(function () {
        let enableAccessed = false;
        if($('div[id^=operations-]').length == 0) return;
        $('div[id^=operations-]').each((index, element) => {
            let that = $(element)
            if (that.hasClass("is-open")) { //说明已经展开了
                tryItOutPaths.forEach(function (url) {
                    let currentNodeUrl = that.find('.opblock-summary-path').text().trim().toLowerCase();
                    enableAccessed = url.toLowerCase().indexOf(currentNodeUrl) != -1
                    if (isProd && !enableAccessed) {
                        if(that.find(".try-out__btn").get(0) === undefined) {
                            let tempTask =  setInterval(function () {
                                if(that.find(".try-out__btn").get(0) !== undefined) {
                                    that.find(".try-out__btn").remove();
                                    clearInterval(tempTask)
                                }
                            }, 50)
                        } else {
                            that.find(".try-out__btn").remove();
                        }
                    }
                })
                clearInterval(task1)
            }
        })
    }, 10)


    // 点击选项卡进行过滤操作
    $(document).click(function () {
        let task2 = setInterval(function () {
            let enableAccessed = false;
            if($('div[id^=operations-]').length == 0) return;
            $('div[id^=operations-]').each((index, element) => {
                let that = $(element)
                if (that.hasClass("is-open")) { //说明已经展开了
                    tryItOutPaths.forEach(function (url) {
                        let currentNodeUrl = that.find('.opblock-summary-path').text().trim().toLowerCase();
                        enableAccessed = url.toLowerCase().indexOf(currentNodeUrl) != -1
                        if (isProd && !enableAccessed) {
                            if(that.find(".try-out__btn").get(0) === undefined) {
                                let tempTask =  setInterval(function () {
                                    if(that.find(".try-out__btn").get(0) !== undefined) {
                                        that.find(".try-out__btn").remove();
                                        clearInterval(tempTask)
                                    }
                                }, 50)
                            } else {
                                that.find(".try-out__btn").remove();
                            }
                        }
                    })
                    clearInterval(task2)
                }
            })
        }, 10);
    })
})