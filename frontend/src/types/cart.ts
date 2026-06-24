export interface CartItem {
  itemId: string;
  name: string;
  imageUrl: string;
  price: number;
  quantity: number;
  subtotal: number;
}

export interface Cart {
  id: string;
  items: CartItem[];
  total: number;
}
