$(function () {
    var tryItOutPaths = [];
    var isProd = false;
    $.ajax({
        url: 'api/swagger/tio',
        method: 'get',
        async: false,
        success: function (res) {
            tryItOutPaths = res.data || [];
            isProd = res.env != 'dev';
        }
    })
    $(document).click(function () {
        let task2 = setInterval(function () {
            let nodes = $('.opblock-tag-section a>span').filter(function () {
                let that = $(this);
                if(that.text().indexOf("/") != -1){
                    return that;
                }
            })
            if( nodes.length > 0) {
                $('.opblock-tag-section a>span').each(function () {
                    let that = $(this);
                    let show = false;
                    tryItOutPaths.forEach(function (item) {
                        show = item.toLowerCase().indexOf(that.text().trim().toLowerCase()) != -1
                        if(show) return;
                    })
                    if (isProd && !show) that.parents('div[id^=operations-]').find(".try-out__btn").remove();
                })
                clearInterval(task2)
            }
        }, 10)

        let task1 = setInterval(function () {
            if ($('.opblock-summary').length > 0) {
                $('.opblock-summary').click(function () {
                    let show = false;
                    tryItOutPaths.forEach(function (item) {
                        show = item.toLowerCase().indexOf($($(this).find('a span').get(0)).text().trim().toLowerCase()) != -1
                        if(show) return;
                    })
                    let node = $(this);
                    let btn = node.parent().find('.try-out__btn');
                    if(btn.length > 0 && !show && tryItOutPaths.length!=0) btn.remove();
                    setTimeout(function () {
                        if (isProd && !show) node.parent().find('.try-out__btn').remove();
                    }, 10)
                })
                clearInterval(task1)
            }
        }, 10)
    })

    let task = setInterval(function () {
        if ($('.opblock-summary').length > 0) {
            $('.opblock-summary').click(function () {
                let show = false;
                tryItOutPaths.forEach(function (item) {
                    show = item.toLowerCase().indexOf($($(this).find('a span').get(0)).text().trim().toLowerCase()) != -1
                    if(show) return;
                })
                let node = $(this);
                let btn = node.parent().find('.try-out__btn');
                if(btn.length > 0 && !show && tryItOutPaths.length!=0) btn.remove();
                setTimeout(function () {
                    if (isProd && !show) node.parent().find('.try-out__btn').remove();
                }, 10)
            })
            clearInterval(task)
        }
    }, 10)
})