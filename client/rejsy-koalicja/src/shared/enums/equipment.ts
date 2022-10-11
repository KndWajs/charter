export enum Equipment {
    SPINNAKER = "SPINNAKER",
    BEDDING = "BEDDING",
}

interface EquipmentType {
    name: string
    cost: number
}

const EquipmentProperties = new Map<Equipment, EquipmentType>([
    [Equipment.SPINNAKER, {name: "Spineker", cost: 500}],
    [Equipment.BEDDING, {name: "Bedding", cost: 100}]
])

export const getEquipmentName = (equipment: Equipment): string | undefined =>
    EquipmentProperties.get(equipment)?.name

export const getEquipmentColor = (equipment: Equipment): number | undefined =>
    EquipmentProperties.get(equipment)?.cost
