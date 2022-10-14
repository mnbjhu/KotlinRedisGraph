package schemas

import UnitNode

class EnumSchema: UnitNode() {
    val enum by serializable<MyEnum>()
}