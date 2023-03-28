# 项目说明
该项目持续维护，提供平时业务上所需要的组件库。

已经实现功能：
- [x] 返回前端自动填充枚举值的描述



## 枚举填充
使用说明:

1. 启动类加入注解，修改枚举扫描路径@EnableEnumFill(basePackages = "com.zeekrlife.ota.sample")
2. 对于需要自动填充的枚举实现 GlobalEnum 接口
3. 返回前端的VO对象属性上加上注解@EnumValue(enumType = TestStatusEnum.class)

不加@EnumValue 注解时返回：
```json
{
    "code": 100000,
    "msg": null,
    "data": {
        "id": "1",
        "userName": "test",
        "status": 1,
        "code": 2
    }
}
```
加入后返回样例：
```json
{
    "code": 100000,
    "msg": null,
    "data": {
        "id": "1",
        "userName": "test",
        "status": 1,
        "code": 2,
        "codeName": "状态",
        "statusName": "测试"
    }
}
```



---
项目持续更新，欢迎大家一起维护～ thx