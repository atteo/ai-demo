import { Container, Title, Table, Image, Text, Group, NumberInput, Button, Stack, Divider, Center } from '@mantine/core'
import { IconTrash } from '@tabler/icons-react'
import { useNavigate } from 'react-router-dom'
import { useCartStore } from '@/store/useCartStore'

export default function CartPage() {
  const { cart, updateQuantity, removeItem } = useCartStore()
  const navigate = useNavigate()
  const items = cart?.items ?? []

  if (items.length === 0) {
    return (
      <Container size="lg" py="xl">
        <Title order={1} mb="xl">Cart</Title>
        <Center h={200}>
          <Stack align="center" gap="md">
            <Text c="dimmed" size="lg">Your cart is empty.</Text>
            <Button onClick={() => navigate('/')}>Browse Products</Button>
          </Stack>
        </Center>
      </Container>
    )
  }

  return (
    <Container size="lg" py="xl">
      <Title order={1} mb="xl">Cart</Title>
      <Table verticalSpacing="md" horizontalSpacing="md">
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Product</Table.Th>
            <Table.Th>Price</Table.Th>
            <Table.Th>Quantity</Table.Th>
            <Table.Th>Subtotal</Table.Th>
            <Table.Th />
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {items.map(item => (
            <Table.Tr key={item.itemId}>
              <Table.Td>
                <Group gap="sm" wrap="nowrap">
                  <Image src={item.imageUrl} w={60} h={60} radius="sm" alt={item.name} />
                  <Text fw={600} lineClamp={2}>{item.name}</Text>
                </Group>
              </Table.Td>
              <Table.Td>
                <Text>${item.price.toFixed(2)}</Text>
              </Table.Td>
              <Table.Td>
                <NumberInput
                  w={80}
                  size="sm"
                  min={1}
                  value={item.quantity}
                  onChange={val => {
                    if (typeof val === 'number' && val >= 1) {
                      updateQuantity(item.itemId, val)
                    }
                  }}
                />
              </Table.Td>
              <Table.Td>
                <Text fw={600}>${item.subtotal.toFixed(2)}</Text>
              </Table.Td>
              <Table.Td>
                <Button variant="subtle" color="red" size="sm" px={8} onClick={() => removeItem(item.itemId)}>
                  <IconTrash size={16} />
                </Button>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>

      <Divider my="xl" />

      <Group justify="flex-end">
        <Stack align="flex-end" gap="md">
          <Group>
            <Text size="xl" fw={700}>Total:</Text>
            <Text size="xl" fw={700} c="blue">${cart!.total.toFixed(2)}</Text>
          </Group>
          <Button size="lg">Checkout</Button>
        </Stack>
      </Group>
    </Container>
  )
}
