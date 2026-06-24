import axios from 'axios'
import type { Item } from '@/types/item'

export const getItems = (): Promise<Item[]> =>
  axios.get<Item[]>('/api/items').then(r => r.data)

export const getItem = (id: string): Promise<Item> =>
  axios.get<Item>(`/api/items/${id}`).then(r => r.data)
