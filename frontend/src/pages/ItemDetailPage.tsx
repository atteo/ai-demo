import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {
  Container, Grid, Image, Title, Text, Badge, Button,
  Group, Stack, Divider, Loader, Center, Alert,
} from '@mantine/core'
import { getItem } from '@/api/items'
import type { Item } from '@/types/item'
import { useCartStore } from '@/store/useCartStore'

export default function ItemDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const addItem = useCartStore(s => s.addItem)
  const [item, setItem] = useState<Item | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) return
    getItem(id)
      .then(setItem)
      .catch(() => setError('Product not found.'))
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <Center h="100vh"><Loader size="xl" /></Center>
  if (error || !item) return (
    <Center h="100vh">
      <Stack align="center">
        <Alert color="red" title="Error">{error ?? 'Product not found.'}</Alert>
        <Button variant="subtle" onClick={() => navigate('/')}>Back to Products</Button>
      </Stack>
    </Center>
  )

  return (
    <Container size="lg" py="xl">
      <Button variant="subtle" mb="xl" onClick={() => navigate('/')}>
        ← Back to Products
      </Button>
      <Grid gutter="xl">
        <Grid.Col span={{ base: 12, md: 6 }}>
          <Image src={item.imageUrl} radius="md" alt={item.name} />
        </Grid.Col>
        <Grid.Col span={{ base: 12, md: 6 }}>
          <Stack gap="md">
            <Badge size="lg" variant="light" color="blue" w="fit-content">
              {item.category}
            </Badge>
            <Title order={1}>{item.name}</Title>
            <Text size="2rem" fw={700} c="blue">
              ${item.price.toFixed(2)}
            </Text>
            <Divider />
            <Text size="md" c="dimmed" style={{ lineHeight: 1.7 }}>
              {item.description}
            </Text>
            <Divider />
            <Group>
              <Text fw={600}>Availability:</Text>
              {item.stock > 0
                ? <Text c="green">{item.stock} in stock</Text>
                : <Text c="red">Out of stock</Text>}
            </Group>
            <Button size="lg" disabled={item.stock === 0} mt="sm" onClick={() => addItem(item.id, 1)}>
              Add to Cart
            </Button>
          </Stack>
        </Grid.Col>
      </Grid>
    </Container>
  )
}
