import { Drawer, Stack, Group, Text, Image, Button, NumberInput, Divider, ScrollArea, Center } from '@mantine/core'
import { IconTrash } from '@tabler/icons-react'
import { useNavigate } from 'react-router-dom'
import { useCartStore } from '@/store/useCartStore'

export default function CartDrawer() {
  const { cart, drawerOpen, closeDrawer, updateQuantity, removeItem } = useCartStore()
  const navigate = useNavigate()
  const items = cart?.items ?? []

  return (
    <Drawer
      opened={drawerOpen}
      onClose={closeDrawer}
      position="right"
      size="md"
      title="Your Cart"
    >
      {items.length === 0 ? (
        <Center h={200}>
          <Text c="dimmed">Your cart is empty.</Text>
        </Center>
      ) : (
        <Stack h="calc(100vh - 120px)" justify="space-between">
          <ScrollArea flex={1}>
            <Stack gap="md">
              {items.map(item => (
                <Group key={item.itemId} align="flex-start" wrap="nowrap">
                  <Image src={item.imageUrl} w={60} h={60} radius="sm" alt={item.name} />
                  <Stack gap={4} flex={1}>
                    <Text fw={600} size="sm" lineClamp={2}>{item.name}</Text>
                    <Text size="sm" c="blue">${item.price.toFixed(2)}</Text>
                    <Group gap="xs">
                      <NumberInput
                        size="xs"
                        w={70}
                        min={1}
                        value={item.quantity}
                        onChange={val => {
                          if (typeof val === 'number' && val >= 1) {
                            updateQuantity(item.itemId, val)
                          }
                        }}
                      />
                      <ActionIconTrash onClick={() => removeItem(item.itemId)} />
                    </Group>
                  </Stack>
                  <Text fw={600} size="sm" style={{ whiteSpace: 'nowrap' }}>
                    ${item.subtotal.toFixed(2)}
                  </Text>
                </Group>
              ))}
            </Stack>
          </ScrollArea>

          <Stack gap="sm">
            <Divider />
            <Group justify="space-between">
              <Text fw={700} size="lg">Total</Text>
              <Text fw={700} size="lg" c="blue">${cart!.total.toFixed(2)}</Text>
            </Group>
            <Button
              variant="light"
              fullWidth
              onClick={() => { closeDrawer(); navigate('/cart') }}
            >
              View Full Cart
            </Button>
            <Button fullWidth>Checkout</Button>
          </Stack>
        </Stack>
      )}
    </Drawer>
  )
}

function ActionIconTrash({ onClick }: { onClick: () => void }) {
  return (
    <Button variant="subtle" color="red" size="xs" px={6} onClick={onClick}>
      <IconTrash size={14} />
    </Button>
  )
}
